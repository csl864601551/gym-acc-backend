package com.ztl.gym.web.controller.product;

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
import com.ztl.gym.product.domain.Attr;
import com.ztl.gym.product.service.IAttrService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 规格属性Controller
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@RestController
@RequestMapping("/product/attr")
public class AttrController extends BaseController
{
    @Autowired
    private IAttrService attrService;

    /**
     * 查询规格属性列表
     */
    @PreAuthorize("@ss.hasPermi('product:attr:list')")
    @GetMapping("/list")
    public TableDataInfo list(Attr attr)
    {
        startPage();
        List<Attr> list = attrService.selectAttrList(attr);
        return getDataTable(list);
    }

    /**
     * 导出规格属性列表
     */
    @PreAuthorize("@ss.hasPermi('product:attr:export')")
    @Log(title = "规格属性", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Attr attr)
    {
        List<Attr> list = attrService.selectAttrList(attr);
        ExcelUtil<Attr> util = new ExcelUtil<Attr>(Attr.class);
        return util.exportExcel(list, "attr");
    }

    /**
     * 获取规格属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:attr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(attrService.selectAttrById(id));
    }

    /**
     * 新增规格属性
     */
    @PreAuthorize("@ss.hasPermi('product:attr:add')")
    @Log(title = "规格属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Attr attr)
    {
        return toAjax(attrService.insertAttr(attr));
    }

    /**
     * 修改规格属性
     */
    @PreAuthorize("@ss.hasPermi('product:attr:edit')")
    @Log(title = "规格属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Attr attr)
    {
        return toAjax(attrService.updateAttr(attr));
    }

    /**
     * 删除规格属性
     */
    @PreAuthorize("@ss.hasPermi('product:attr:remove')")
    @Log(title = "规格属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(attrService.deleteAttrByIds(ids));
    }

    /**
     * 获取属性值可选值列表
     */
    @GetMapping(value = "/getAttrsById/{id}")
    public AjaxResult getAttrValuesById(@PathVariable("id") Long id)
    {
        return AjaxResult.success(attrService.getAttrValuesById(id));
    }
}
