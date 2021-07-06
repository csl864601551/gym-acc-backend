package com.ztl.gym.web.controller.common;
import java.util.List;
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
import com.ztl.gym.common.domain.AndroidVersion;
import com.ztl.gym.common.service.IAndroidVersionService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 安卓版本信息Controller
 *
 * @author ruoyi
 * @date 2021-07-06
 */
@RestController
@RequestMapping("/version/version")
public class AndroidVersionController extends BaseController
{
    @Autowired
    private IAndroidVersionService androidVersionService;

    /**
     * 查询安卓版本信息列表
     */
    @PreAuthorize("@ss.hasPermi('version:version:list')")
    @GetMapping("/list")
    public TableDataInfo list(AndroidVersion androidVersion)
    {
        startPage();
        List<AndroidVersion> list = androidVersionService.selectAndroidVersionList(androidVersion);
        return getDataTable(list);
    }

    /**
     * 导出安卓版本信息列表
     */
    @PreAuthorize("@ss.hasPermi('version:version:export')")
    @Log(title = "安卓版本信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AndroidVersion androidVersion)
    {
        List<AndroidVersion> list = androidVersionService.selectAndroidVersionList(androidVersion);
        ExcelUtil<AndroidVersion> util = new ExcelUtil<AndroidVersion>(AndroidVersion.class);
        return util.exportExcel(list, "version");
    }

    /**
     * 获取安卓版本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('version:version:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(androidVersionService.selectAndroidVersionById(id));
    }

    /**
     * 新增安卓版本信息
     */
    @PreAuthorize("@ss.hasPermi('version:version:add')")
    @Log(title = "安卓版本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AndroidVersion androidVersion)
    {
        return toAjax(androidVersionService.insertAndroidVersion(androidVersion));
    }

    /**
     * 修改安卓版本信息
     */
    @PreAuthorize("@ss.hasPermi('version:version:edit')")
    @Log(title = "安卓版本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AndroidVersion androidVersion)
    {
        return toAjax(androidVersionService.updateAndroidVersion(androidVersion));
    }

    /**
     * 删除安卓版本信息
     */
    @PreAuthorize("@ss.hasPermi('version:version:remove')")
    @Log(title = "安卓版本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(androidVersionService.deleteAndroidVersionByIds(ids));
    }
}
