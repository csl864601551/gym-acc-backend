package com.ztl.gym.web.controller.code;

import java.util.List;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAcc;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.code.domain.CodeAccRecord;
import com.ztl.gym.code.service.ICodeAccRecordService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 生码记录Controller
 *
 * @author ruoyi
 * @date 2021-07-22
 */
@RestController
@RequestMapping("/code/acc")
public class CodeAccRecordController extends BaseController
{
    @Autowired
    private ICodeAccRecordService codeAccRecordService;

    @Value("${ruoyi.preAccUrl}")
    private String preAccUrl;
    /**
     * 查询生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:acc:list')")
    @GetMapping("/list")
    public TableDataInfo list(CodeAccRecord codeAccRecord)
    {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeAccRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<CodeAccRecord> list = codeAccRecordService.selectCodeAccRecordList(codeAccRecord);
        return getDataTable(list);
    }

    /**
     * 导出生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:acc:export')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CodeAccRecord codeAccRecord)
    {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeAccRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<CodeAccRecord> list = codeAccRecordService.selectCodeAccRecordList(codeAccRecord);
        ExcelUtil<CodeAccRecord> util = new ExcelUtil<CodeAccRecord>(CodeAccRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取生码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:acc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(codeAccRecordService.selectCodeAccRecordById(id));
    }

    /**
     * 新增生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:acc:add')")
    @Log(title = "生码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CodeAccRecord codeAccRecord)
    {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        return toAjax(codeAccRecordService.createAccCodeSingle(companyId, codeAccRecord.getCount(), codeAccRecord.getRemark()));

    }

    /**
     * 修改生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:acc:edit')")
    @Log(title = "生码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeAccRecord codeAccRecord)
    {
        return toAjax(codeAccRecordService.updateCodeAccRecord(codeAccRecord));
    }

    /**
     * 删除生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:acc:remove')")
    @Log(title = "生码记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(codeAccRecordService.deleteCodeAccRecordByIds(ids));
    }
    /**
     * 根据生码记录id查询码明细
     *
     * @param codeAccRecord
     * @return
     */
    @GetMapping("/listSon")
    public TableDataInfo listSon(CodeAccRecord codeAccRecord) {
        startPage();
        List<CodeAcc> list = codeAccRecordService.selectAccCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeAccRecord.getId());
        return getDataTable(list);
    }
    /**
     * 码下载
     */
    @PreAuthorize("@ss.hasPermi('code:acc:download')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public AjaxResult download(CodeAccRecord codeAccRecord) {
        List<CodeAcc> list =  codeAccRecordService.selectAccCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeAccRecord.getId());
        for (CodeAcc codeAcc : list) {
            codeAcc.setCodeAcc(codeAcc.getCodeAcc());
            codeAcc.setCodeTypeName("防伪码");
        }
        ExcelUtil<CodeAcc> util = new ExcelUtil<CodeAcc>(CodeAcc.class);
        return util.exportExcel(list,"-"+ DateUtils.getDate()+"防伪码");
    }

    /**
     * 码下载TXT
     */
    @PreAuthorize("@ss.hasPermi('code:acc:download')")
    @GetMapping("/downloadTxt")
    public AjaxResult downloadTxt(CodeAccRecord codeAccRecord, HttpServletResponse response) {
        List<CodeAcc> list =  codeAccRecordService.selectAccCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeAccRecord.getId());
        String temp = "防伪码" + "                                        " + "\r\n";
        for (CodeAcc codeAcc : list) {
            temp += "        " + preAccUrl+codeAcc.getCodeAcc() + "\r\n";
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", temp);
        return ajax;
    }
}
