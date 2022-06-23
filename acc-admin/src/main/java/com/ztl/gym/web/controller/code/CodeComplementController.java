package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/code/complement")
public class CodeComplementController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CodeRecordController.class);

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ICodeAttrService tCodeAttrService;

    /**
     * 补码首页
     */
    @PreAuthorize("@ss.hasPermi('code:complement:list')")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        return null;
    }

    /**
     * 补码操作
     * @param productCode
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:complement:create')")
    @GetMapping("/{productCode}")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult checkCode(@PathVariable("productCode") String productCode) {
        if (productCode != null) {
            Code codeParam = new Code();
            codeParam.setCode(productCode);
            codeParam.setCompanyId(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            Code code = codeService.selectCode(codeParam);   //查询单码数据
            if(code !=null) {
                //获取产品信息
                CodeAttr codeAttr = tCodeAttrService.selectCodeAttrById(code.getCodeAttrId());
                Map<String, Object> params = new HashMap<>();
                params.put("productNo", codeAttr.getProductNo());
                params.put("productName", codeAttr.getProductName());
                return AjaxResult.success(params);
            } else {
                throw new CustomException("产品码不存在！");
            }
        } else {
            throw new CustomException("产品码为空！");
        }
    }
}
