package com.ztl.gym.web.controller.storage;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.StorageOutExport;
import com.ztl.gym.storage.service.IStorageOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 出库Controller
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/storage/out")
public class StorageOutController extends BaseController
{
    @Autowired
    private IStorageOutService storageOutService;

    /**
     * 查询出库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:out:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageOut storageOut)
    {
        startPage();
        List<StorageOut> list = storageOutService.selectStorageOutList(storageOut);
        return getDataTable(list);
    }

    /**
     * 导出出库列表
     */
//    @PreAuthorize("@ss.hasPermi('storage:out:export')")
    @Log(title = "出库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageOut storageOut)
    {
        storageOut.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        List<StorageOutExport> list = storageOutService.selectStorageOutExport(storageOut);
        ExcelUtil<StorageOutExport> util = new ExcelUtil<StorageOutExport>(StorageOutExport.class);
        return util.exportExcel(list, "out");
    }

    /**
     * 获取出库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:out:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(storageOutService.selectStorageOutById(id));
    }
    /**
     * 获取出库码产品详细信息
     */
    @GetMapping("/getCodeDetailById")
    public AjaxResult getCodeDetailById(@RequestParam("companyId") Long companyId, @RequestParam("id") Integer id) {
        return AjaxResult.success(storageOutService.getCodeDetailById(companyId,id));
    }

    /**
     * 新增出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:add')")
    @Log(title = "出库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageOut storageOut)
    {
        //storageOut.setExtraNo("DB10220210707101654");
        storageOutService.insertStorageOut(storageOut);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", storageOut.getId());
        return ajax;
    }

    /**
     * 新增出库（客户端）
     */
    @PostMapping("/tenantOut")
    public AjaxResult tenantOut(@RequestBody Map<String, Object> map) {
        List<String> codes = new ArrayList<>();
        codes.add(map.get("codes").toString());

        StorageOut storageOut = new StorageOut();
        storageOut.setOutNo(map.get("outNo").toString());
        storageOut.setProductId(Long.valueOf(map.get("productId").toString()));
        storageOut.setStorageTo(Long.valueOf(map.get("storageTo").toString()));
        storageOut.setFromStorageId(Long.valueOf(map.get("fromStorageId").toString()));
        storageOut.setThirdPartyFlag(map.get("thirdPartyFlag").toString());
        storageOut.setCodes(codes);
        storageOutService.insertStorageOut(storageOut);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", storageOut.getId());
        return ajax;
    }

    /**
     * 修改出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:edit')")
    @Log(title = "出库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageOut storageOut)
    {
        return toAjax(storageOutService.updateStorageOut(storageOut));
    }

    /**
     * 删除出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:remove')")
    @Log(title = "出库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storageOutService.deleteStorageOutByIds(ids));
    }

    /**
     * 撤销出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:remove')")
    @Log(title = "出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/back/{id}")
    public AjaxResult back(@PathVariable Long id)
    {
        return toAjax(storageOutService.backStorageOutById(id,StorageOut.STATUS_CANCEL));
    }

    /**
     * updateInStatusByCode
     */
    /**
     * PDA出库，执行更新出库表，调用addCodeFlow,插入入库表
     */
    @Log(title = "PDA扫码出库", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/updateOutStatusByCode")
    public AjaxResult updateOutStatusByCode(@RequestBody Map<String, Object> map)
    {
        return toAjax(storageOutService.updateOutStatusByCode(map));
    }
    /**
     * 统计当日已出库产品数量
     */
    @PostMapping("/dayCount")
    public AjaxResult dayCount(@RequestBody Map<String, Object> map)
    {
        int count=0;
        List<Map<String,Object>> list = storageOutService.selectDayCount(map);
        if(list.size()>0){
            if(list.get(0).get("num")!=null){
                count=Integer.valueOf(list.get(0).get("num").toString());
            }
        }
        return AjaxResult.success(count);
    }
}
