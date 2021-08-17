package com.ztl.gym.web.controller.product;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Attr;
import com.ztl.gym.product.service.IAttrService;
import com.ztl.gym.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ISysUserService userService;

    /**
     * 查询规格属性列表
     */
    @PreAuthorize("@ss.hasPermi('product:attr:list')")
    @GetMapping("/list")
    public TableDataInfo list(Attr attr)
    {
        List<Attr> lists = new ArrayList<Attr>();
        int total = attrService.selectcountAttrList(attr);
        startPage();
        List<Attr> list = attrService.selectAttrList(attr);
        if(list.size()>0){
            for(Attr attrinfo : list){
                SysUser user = userService.selectUserById(attrinfo.getCreateUser());
                if(user!=null){
                    attrinfo.setCreateUserName(user.getNickName());
                    lists.add(attrinfo);
                }else{
                    lists.add(attrinfo);
                }
            }
        }
        return getDataTables(lists,total);
    }



    /**
     * 查询所有的规格属性列表
     */
    @GetMapping("/alllist")
    public TableDataInfo alllist(Attr attr)
    {
        List<Attr> lists = new ArrayList<Attr>();
        List<Attr> list = attrService.selectAttrList(attr);
        if(list.size()>0){
            for(Attr attrinfo : list){
                SysUser user = userService.selectUserById(attrinfo.getCreateUser());
                if(user!=null){
                    attrinfo.setCreateUserName(user.getNickName());
                    lists.add(attrinfo);
                }else{
                    lists.add(attrinfo);
                }
            }
        }
        return getDataTable(lists);
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
        int message = 0;
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        if(StrUtil.isNotEmpty(attr.getAttrNameCn())){
            Attr attrinfo = attrService.selectAttrBySome(companyId,attr.getAttrNameCn());
            if(attrinfo!=null){
                return error("该属性名称已存在！！！");
            }else{
                message = attrService.insertAttr(attr);
            }
        }
        return toAjax(message);
    }

    /**
     * 修改规格属性
     */
    @PreAuthorize("@ss.hasPermi('product:attr:edit')")
    @Log(title = "规格属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Attr attr)
    {
        if(attr.getInputType()==1){
            attr.setAttrValue("");
        }
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


    /**
     * 获取属性值可选值列表
     */
    @GetMapping(value = "/getAttrsByName/{name}")
    public AjaxResult getAttrValuesById(@PathVariable("name") String name)
    {
        return AjaxResult.success(attrService.selectAttrByName(name));
    }
}
