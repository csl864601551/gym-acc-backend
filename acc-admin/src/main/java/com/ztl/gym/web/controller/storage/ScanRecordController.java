package com.ztl.gym.web.controller.storage;

import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫码记录Controller
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@RestController
@RequestMapping("/storage/record")
public class ScanRecordController extends BaseController {
    @Autowired
    private IScanRecordService scanRecordService;

    @Autowired
    private ISysDeptService deptService;


    /**
     * 查询扫码记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ScanRecord scanRecord) {
        startPage();
        List<ScanRecord> list = scanRecordService.selectScanRecordList(scanRecord);
        return getDataTable(list);
    }

    /**
     * 导出扫码记录列表
     */
    @Log(title = "扫码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ScanRecord scanRecord) {
        List<ScanRecord> list = scanRecordService.selectScanRecordList(scanRecord);
        ExcelUtil<ScanRecord> util = new ExcelUtil<ScanRecord>(ScanRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取扫码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(scanRecordService.selectScanRecordById(id));
    }

    /**
     * 新增扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScanRecord scanRecord) {
        return toAjax(scanRecordService.insertScanRecord(scanRecord));
    }

    /**
     * 修改扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScanRecord scanRecord) {
        return toAjax(scanRecordService.updateScanRecord(scanRecord));
    }

    /**
     * 删除扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(scanRecordService.deleteScanRecordByIds(ids));
    }

    /**
     * 根据码号查询相关产品和码信息
     */
    @GetMapping(value = "/getScanRecordByCode")
    public AjaxResult getScanRecordByCode(@RequestParam("code") String code) {
        return AjaxResult.success(scanRecordService.getScanRecordByCode(CodeRuleUtils.getCompanyIdByCode(code.trim()), code.trim()));
    }


    /**
     * 判断是否窜货
     */
    @GetMapping("/getIsMixInfo")
    public AjaxResult getIsMixInfo(CompanyArea area) {
        CompanyArea res = scanRecordService.getIsMixInfo(area);
        return AjaxResult.success(res);
    }


    /**
     * 根据码号查询产品的物流信息
     */
    @PostMapping(value = "/getFlowListByCode")
    public AjaxResult getFlowListByCode(@RequestParam("code") String code) {
        try {
            List<List<Map<String, Object>>> lists = new ArrayList<List<Map<String, Object>>>();
            List<Map<String, Object>> flowList = scanRecordService.getFlowListByCode(CodeRuleUtils.getCompanyIdByCode(code.trim()), code.trim());
            if (flowList.size() > 0) {
                for (int i = 0; i < flowList.size(); i++) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Map<String, Object> frommap = new HashMap<String, Object>();
                    Map<String, Object> inmap = new HashMap<String, Object>();
                    Map<String, Object> map = flowList.get(i);
                    if (map.get("title_name").equals("入库")) {
                        if (map.get("storage_to") != null) {
                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
                            if (sysDept != null) {
                                boolean status = sysDept.getProvince().contains("市");
                                if (status) {
                                    frommap.put("name", sysDept.getProvince());
                                    inmap.put("name", sysDept.getProvince());
                                    inmap.put("value", 1);
                                    list.add(frommap);
                                    list.add(inmap);
                                } else {
                                    frommap.put("name", sysDept.getCity());
                                    inmap.put("name", sysDept.getCity());
                                    inmap.put("value", 1);
                                    list.add(frommap);
                                    list.add(inmap);
                                }
                            }
                        }
                        lists.add(list);
                    } else {
                        if (map.get("storage_from_id") != null) {
                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_from_id"))));
                            if (sysDept != null) {
                                if (sysDept.getProvince() != null) {
                                    boolean status = sysDept.getProvince().contains("市");
                                    if (status) {
                                        frommap.put("name", sysDept.getProvince());
                                        list.add(frommap);
                                    } else {
                                        frommap.put("name", sysDept.getCity());
                                        list.add(frommap);
                                    }
                                }
                            }
                        }
                        if (map.get("storage_to") != null) {
                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
                            if (sysDept != null) {
                                if (sysDept.getProvince() != null) {
                                    boolean status = sysDept.getProvince().contains("市");
                                    if (status) {
                                        inmap.put("name", sysDept.getProvince());
                                        inmap.put("value", 1);
                                        list.add(inmap);
                                    } else {
                                        inmap.put("name", sysDept.getCity());
                                        inmap.put("value", 1);
                                        list.add(inmap);
                                    }
                                }
                            }
                        }
                        lists.add(list);
                    }
                }
            }
            System.out.println(lists);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("lists", lists);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }


}
