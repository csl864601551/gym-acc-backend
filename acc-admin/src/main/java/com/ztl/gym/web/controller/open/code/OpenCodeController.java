package com.ztl.gym.web.controller.open.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open/code")
public class OpenCodeController {

    @Autowired
    private ICodeService codeService;
    @Autowired
    private CommonService commonService;
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
                throw new CustomException("该码不在当前流转节点！", HttpStatus.NOT_IMPLEMENTED);
            }
            for (Code codes : codeList) {
                String typeName = "未知";
                if (CodeRuleUtils.getCodeType(codes.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
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
}
