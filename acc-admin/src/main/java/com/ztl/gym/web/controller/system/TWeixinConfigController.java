package com.ztl.gym.web.controller.system;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.system.domain.TWeixinConfig;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ITWeixinConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * wxconfigController
 *
 * @author ruoyi
 * @date 2021-06-17
 */
@RestController
@RequestMapping("/system/wxconfig")
public class TWeixinConfigController extends BaseController
{
    @Autowired
    private ITWeixinConfigService tWeixinConfigService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询wxconfig列表
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(TWeixinConfig tWeixinConfig)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            Long deptId = loginUser.getUser().getDeptId();
            if (deptId != 100) {
                tWeixinConfig.setCompanyId(deptId);
            } else {
                tWeixinConfig.setCompanyId(null);
            }
        }
        startPage();
        List<TWeixinConfig> list = tWeixinConfigService.selectTWeixinConfigList(tWeixinConfig);
        return getDataTable(list);
    }

    /**
     * 导出wxconfig列表
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:export')")
    @Log(title = "wxconfig", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TWeixinConfig tWeixinConfig)
    {
        List<TWeixinConfig> list = tWeixinConfigService.selectTWeixinConfigList(tWeixinConfig);
        ExcelUtil<TWeixinConfig> util = new ExcelUtil<TWeixinConfig>(TWeixinConfig.class);
        return util.exportExcel(list, "wxconfig数据");
    }

    /**
     * 获取wxconfig详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(tWeixinConfigService.selectTWeixinConfigById(id));
    }

    /**
     * 新增wxconfig
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:add')")
    @Log(title = "wxconfig", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TWeixinConfig tWeixinConfig)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            Long userid = loginUser.getUser().getUserId();
            String username = loginUser.getUser().getUserName();
            tWeixinConfig.setCreateUser(userid);
            tWeixinConfig.setCreateUserName(username);
        }
        if(tWeixinConfig.getCompanyId()!=null){
            SysDept sysDept = deptService.selectDeptById(tWeixinConfig.getCompanyId());
            if(sysDept!=null){
                tWeixinConfig.setCompanyName(sysDept.getDeptName());
            }
        }
        return toAjax(tWeixinConfigService.insertTWeixinConfig(tWeixinConfig));
    }

    /**
     * 修改wxconfig
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:edit')")
    @Log(title = "wxconfig", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TWeixinConfig tWeixinConfig)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            Long userid = loginUser.getUser().getUserId();
            String username = loginUser.getUser().getUserName();
            tWeixinConfig.setUpdateUser(userid);
            tWeixinConfig.setUpdateUserName(username);
        }
        if(tWeixinConfig.getCompanyId()!=null){
            SysDept sysDept = deptService.selectDeptById(tWeixinConfig.getCompanyId());
            if(sysDept!=null){
                tWeixinConfig.setCompanyName(sysDept.getDeptName());
            }
        }
        return toAjax(tWeixinConfigService.updateTWeixinConfig(tWeixinConfig));
    }

    /**
     * 删除wxconfig
     */
    @PreAuthorize("@ss.hasPermi('system:wxconfig:remove')")
    @Log(title = "wxconfig", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(tWeixinConfigService.deleteTWeixinConfigByIds(ids));
    }
}
