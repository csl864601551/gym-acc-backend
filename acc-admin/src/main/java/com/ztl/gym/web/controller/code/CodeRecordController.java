package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/code/record")
public class CodeRecordController extends BaseController {
    @Autowired
    private ICodeRecordService codeRecordService;
    @Autowired
    private ICodeService codeService;
    /**
     * 查询生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(CodeRecord codeRecord) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<CodeRecord> list = codeRecordService.selectCodeRecordList(codeRecord);
        return getDataTable(list);
    }

    /**
     * 导出生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:record:export')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CodeRecord codeRecord) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<CodeRecord> list = codeRecordService.selectCodeRecordList(codeRecord);
        for (CodeRecord record : list) {
            String statusName = "";
            if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
                statusName = "创建中";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
                statusName = "待赋值";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
                statusName = "已赋值";
            }
            record.setStatusName(statusName);

            String typeName = "";
            if (record.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
                typeName = "普通生码";
            } else if (record.getType() == AccConstants.GEN_CODE_TYPE_BOX) {
                typeName = "套标生码";
            }
            record.setTypeName(typeName);
        }
        ExcelUtil<CodeRecord> util = new ExcelUtil<CodeRecord>(CodeRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取生码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CodeRecord codeRecord = codeRecordService.selectCodeRecordById(id);
        CodeRecordDetailVo vo = new CodeRecordDetailVo();
        if (codeRecord.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
            vo.setType("普通");
            vo.setSizeNum("单码：" + codeRecord.getCount());
        } else {
            vo.setType("套标");
            vo.setSizeNum("箱码：1 " + "单码：" + codeRecord.getCount());
        }
        vo.setProductName(codeRecord.getProductName());
        vo.setBatchNo(codeRecord.getBatchNo());
        vo.setBarCode(codeRecord.getBarCode());
        vo.setCodeIndexs(codeRecord.getIndexStart() + "~" + codeRecord.getIndexEnd());

        Code code = new Code();
//        code.set
//        codeService.selectCodeList()
        return AjaxResult.success(vo);
    }

//    /**
//     * 新增生码记录
//     */
//    @PreAuthorize("@ss.hasPermi('code:record:add')")
//    @Log(title = "生码记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody CodeRecord codeRecord) {
//        return toAjax(codeRecordService.insertCodeRecord(codeRecord));
//    }

    /**
     * 修改生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:record:edit')")
    @Log(title = "生码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeRecord codeRecord) {
        return toAjax(codeRecordService.updateCodeRecord(codeRecord));
    }

    /**
     * 删除生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:record:remove')")
    @Log(title = "生码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(codeRecordService.deleteCodeRecordByIds(ids));
    }

    /**
     * 生码
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:add')")
    @Log(title = "生码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CodeRecord codeRecord) {
        if (codeRecord.getType().equals(AccConstants.GEN_CODE_TYPE_SINGLE)) {
            return toAjax(codeRecordService.createCodeRecord(SecurityUtils.getLoginUserCompany().getDeptId(), codeRecord.getCount(), codeRecord.getRemark()));
        } else {
            return toAjax(codeRecordService.createPCodeRecord(SecurityUtils.getLoginUserCompany().getDeptId(), codeRecord.getCount(), codeRecord.getRemark()));
        }
    }


}
