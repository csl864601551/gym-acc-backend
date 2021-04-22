package com.ztl.gym.web.controller.storage;

import java.util.List;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.service.IStorageService;
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
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.service.IStorageBackService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 退货Controller
 *
 * @author ruoyi
 * @date 2021-04-19
 */
@RestController
@RequestMapping("/storage/back")
public class StorageBackController extends BaseController {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageBackService storageBackService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ICodeAttrService codeAttrService;
    @Autowired
    private CommonService commonService;

    /**
     * 查询退货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:back:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageBack storageBack) {
        startPage();
        List<StorageBack> list = storageBackService.selectStorageBackList(storageBack);
        return getDataTable(list);
    }

    /**
     * 导出退货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:back:export')")
    @Log(title = "退货", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageBack storageBack) {
        List<StorageBack> list = storageBackService.selectStorageBackList(storageBack);
        ExcelUtil<StorageBack> util = new ExcelUtil<StorageBack>(StorageBack.class);
        return util.exportExcel(list, "back");
    }

    /**
     * 获取退货详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:back:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storageBackService.selectStorageBackById(id));
    }

    /**
     * 退货录入-步骤1 【生成退货单号】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:addFirst')")
    @PostMapping(value = "/addFirst")
    public AjaxResult addFirst(@RequestBody StorageBack storageBack) {
        String backNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_BACK);

        int backType = storageBack.getBackType();
        if(backType == AccConstants.BACK_TYPE_COMMON) {
            //普通退货
            int type = CodeRuleUtils.judgeCode(storageBack.getCodeStr());
            if (type == AccConstants.NO_TYPE_CODE_P) {
                Code code = new Code();
                code.setCode(storageBack.getCodeStr());
                Code pCode = codeService.selectCode(code);
                int storageType = pCode.getCodeAttr().getStorageType();
                long storageRecordId = pCode.getCodeAttr().getStorageRecordId();

            } else if (type == AccConstants.NO_TYPE_CODE_S) {

            }

        }else if(backType == AccConstants.BACK_TYPE_TRANSFER) {
            //调拨退货-经销商平级退货

        }



        return AjaxResult.success();
    }

    /**
     * 退货录入-步骤2 【退货单更新仓库、备注信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:add')")
    @Log(title = "退货", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageBack storageBack) {
        return toAjax(storageBackService.insertStorageBack(storageBack));
    }

    /**
     * 修改退货
     */
    @PreAuthorize("@ss.hasPermi('storage:back:edit')")
    @Log(title = "退货", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageBack storageBack) {
        return toAjax(storageBackService.updateStorageBack(storageBack));
    }

    /**
     * 删除退货
     */
    @PreAuthorize("@ss.hasPermi('storage:back:remove')")
    @Log(title = "退货", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageBackService.deleteStorageBackByIds(ids));
    }
}
