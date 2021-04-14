package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code/record")
public class CodeRecordController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CodeRecordController.class);

    //    @Autowired
//    private ICodeService codeService;
    @Autowired
    private ICodeRecordService codeRecordService;

    /**
     * 普通生码
     *
     * @param num
     * @param remark
     * @return
     */
    //    @PreAuthorize("@ss.hasPermi('code:gen:add')")
    @Log(title = "生码管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(long num, String remark) {
//        return toAjax(codeService.createCode(SecurityUtils.getLoginUserDept().getDeptId(), (long) code.getParams().get("num"), (String) code.getParams().get("remark")));
        return toAjax(codeRecordService.createCodeRecord(100, num, remark));
    }

    /**
     * 套标生码
     *
     * @param num
     * @param remark
     * @return
     */
    //    @PreAuthorize("@ss.hasPermi('code:gen:add')")
    @Log(title = "生码管理", businessType = BusinessType.INSERT)
    @PostMapping("/addP")
    public AjaxResult addP(long num, String remark) {
        return toAjax(codeRecordService.createPCodeRecord(100, num, remark));
    }


}
