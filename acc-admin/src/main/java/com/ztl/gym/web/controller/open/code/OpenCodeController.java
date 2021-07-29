package com.ztl.gym.web.controller.open.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open/code")
public class OpenCodeController {

    @Autowired
    private ICodeService codeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeAttrService codeAttrService;
    /**
     * 获取所有码
     *
     * @return 获取所有码
     */
    @PostMapping("getCodes")
    public AjaxResult getCodes(@RequestBody Map<String,Object> map) {
        map.put("companyId",Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
        List<Code> codeList = codeService.selectCodeListByCodeOrIndex(map);
        if(codeList.size()>0){
            String code=codeList.get(0).getCode();
            if (!commonService.judgeStorageIsIllegalByValue(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()), Integer.valueOf(map.get("storageType").toString()), code)) {
                throw new CustomException("该码不在当前流转节点！", HttpStatus.ERROR);
            }
            for (Code codes : codeList) {
                String typeName = "未知";
                if (codes.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                    typeName = "箱码";
                } else {
                    typeName = "单码";
                }
                codes.setCodeTypeName(typeName);
            }
            return AjaxResult.success(codeList);
        }else{
            throw new CustomException("未查询到码数据！", HttpStatus.ERROR);
        }

    }

    /**
     * 入库前判定码是否赋值，未赋值则进行赋值操作
     * @param map
     * @return
     */
    @PostMapping("/checkCodeAttr")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult checkCodeAttr(@RequestBody Map<String,Object> map) {
        if(map != null) {
            map.put("companyId",Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            List<Code> codeList = codeService.selectCodeListByCodeOrIndex(map);
            if(codeList.size()>0){
                String code=codeList.get(0).getCode();
                if (!commonService.judgeStorageIsIllegalByValue(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()), Integer.valueOf(map.get("storageType").toString()), code)) {
                    throw new CustomException("该码不在当前流转节点！", HttpStatus.ERROR);
                }
            }else{
                throw new CustomException("未查询到码数据！", HttpStatus.ERROR);
            }
            Code codeStart=codeList.stream().min(Comparator.comparing(Code::getCodeIndex)).get();
            long indexStart = codeStart.getCodeIndex();
            long indexEnd = codeList.stream().max(Comparator.comparing(Code::getCodeIndex)).get().getCodeIndex();
            boolean isProduct=Boolean.valueOf(map.get("isProduct").toString());//码属性是否已经赋值
            if(isProduct){
                if(codeStart.getCodeAttr()==null){
                    throw new CustomException("该码尚未赋值！", HttpStatus.ERROR);
                }else{
                    if(codeStart.getCodeAttr().getProductId()==null){
                        throw new CustomException("该码尚未赋值！", HttpStatus.ERROR);
                    }
                    if(Long.valueOf(map.get("productId").toString())!=codeStart.getCodeAttr().getProductId()){
                        throw new CustomException("该码属性赋值产品与选择产品不一致！", HttpStatus.ERROR);
                    }
                }
            }else {
                CodeAttr codeAttr = new CodeAttr();
                codeAttr.setIndexStart(indexStart);
                codeAttr.setIndexEnd(indexEnd);
                if(codeStart.getSingleId()!=null){
                    codeAttr.setSingleId(codeStart.getSingleId());
                }
                codeAttr.setCompanyId(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
                try {
                    codeAttr.setProductId(Long.valueOf(map.get("productId").toString()));
                    codeAttr.setProductNo(map.get("productNo").toString());
                    codeAttr.setProductName(map.get("productName").toString());
                    codeAttr.setBatchId(Long.valueOf(map.get("batchId").toString()));
                    codeAttr.setBatchNo(map.get("batchNo").toString());
                }catch (Exception e){
                    throw new CustomException("未获取到产品批次完整信息！", HttpStatus.ERROR);
                }
                if(codeStart.getCodeAttr()==null){//单码处理

                    //插入编码属性表
                    Long codeAttrId = codeAttrService.insertCodeAttr(codeAttr);
                    //更新编码信息表
                    Map<String, Object> params = new HashMap<>();
                    params.put("pCode", map.get("code"));
                    params.put("codeAttrId", Long.valueOf(codeAttrId));
                    params.put("companyId", Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
                    codeService.updateCodeAttrIdByPCode(params);
                }else{
                    if(codeStart.getCodeAttr().getProductId()==null){//箱码处理
                        //更新编码属性表
                        codeAttr.setId(codeStart.getCodeAttrId());
                        codeAttrService.updateCodeAttr(codeAttr);
                    }else{
                        throw new CustomException("该码已赋值！", HttpStatus.ERROR);
                    }
                }
            }



            return AjaxResult.success(codeList);
        } else {
            throw new CustomException("产品信息为空！",HttpStatus.ERROR);
        }
    }
}
