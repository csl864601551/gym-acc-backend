package com.ztl.gym.web.controller.mix;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.common.utils.ip.IpUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.mix.domain.MixRecord;
import com.ztl.gym.mix.service.IMixRecordService;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 窜货记录Controller
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@RestController
@RequestMapping("/mix/record")
public class MixRecordController extends BaseController
{
    @Autowired
    private IMixRecordService mixRecordService;
    @Autowired
    private IScanRecordService scanRecordService;

    /**
     * 查询窜货记录列表
     */
    @PreAuthorize("@ss.hasPermi('mix:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(MixRecord mixRecord)
    {
        startPage();
        List<MixRecord> list = mixRecordService.selectMixRecordList(mixRecord);
        return getDataTable(list);
    }


    /**
     * 查询窜货记录列表
     */
    @PreAuthorize("@ss.hasPermi('mix:record:list')")
    @GetMapping("/list1")
    public TableDataInfo list1(MixRecord mixRecord)
    {
        List<MixRecord> list = mixRecordService.selectMixRecordList(mixRecord);
        return getDataTable(list);
    }

    /**
     * 导出窜货记录列表
     */
    @Log(title = "窜货记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MixRecord mixRecord)
    {
        List<MixRecord> list = mixRecordService.selectMixRecordList(mixRecord);
        ExcelUtil<MixRecord> util = new ExcelUtil<MixRecord>(MixRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取窜货记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('mix:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(mixRecordService.selectMixRecordById(id));
    }

    /**
     * 新增窜货记录
     */
    @Log(title = "窜货记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MixRecord mixRecord)
    {
        return toAjax(mixRecordService.insertMixRecord(mixRecord));
    }

    /**
     * 修改窜货记录
     */
    @Log(title = "窜货记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MixRecord mixRecord)
    {
        return toAjax(mixRecordService.updateMixRecord(mixRecord));
    }

    /**
     * 删除窜货记录
     */
    @Log(title = "窜货记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mixRecordService.deleteMixRecordByIds(ids));
    }

    /**
     * 合并新增窜货记录和扫码记录
     */
    @Log(title = "新增窜货记录和扫码记录", businessType = BusinessType.INSERT)
    @PostMapping("/addRecord")
    public AjaxResult addRecord(@RequestBody Map<String,Object> map)
    {
        try {
            ScanRecord scanRecord=new ScanRecord();
            MixRecord mixRecord=new MixRecord();
            scanRecord.setCode(map.get("code").toString());
            scanRecord.setIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            if(map.get("province")!=null){
                scanRecord.setProvince(map.get("province").toString());
                scanRecord.setCity(map.get("city").toString());
                scanRecord.setArea(map.get("area").toString());
                scanRecord.setAddress(map.get("address").toString());
                mixRecord.setAreaMix(map.get("province").toString()+map.get("city").toString()+map.get("area").toString());
            }
            scanRecordService.insertScanRecord(scanRecord);
            mixRecord.setTenantId(Long.valueOf(map.get("tenantId").toString()));
            mixRecord.setCode(map.get("code").toString());
            mixRecord.setMixType(Integer.valueOf(map.get("mixType").toString()));
            mixRecord.setMixFrom(Integer.valueOf(map.get("mixFrom").toString()));
            mixRecord.setProductId(Long.valueOf(map.get("productId").toString()));
            mixRecord.setBatchId(Long.valueOf(map.get("batchId").toString()));
            mixRecord.setAreaOld(map.get("areaOld").toString());
            return toAjax(mixRecordService.insertMixRecord(mixRecord));
        }catch (Exception e){
            return toAjax(0);
        }
    }
}
