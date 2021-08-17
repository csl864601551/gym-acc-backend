package com.ztl.gym.web.controller.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private IProductService tProductService;



    /**
     * 查询扫码记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ScanRecord scanRecord) {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            scanRecord.setCompanyId(company_id);
        }
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
        if(scanRecord.getProductId()!=null){
            if(scanRecord.getProductId()>0){
                Product product = tProductService.selectTProductByIdOne(scanRecord.getProductId());
                if(product!=null){
                    scanRecord.setProductName(product.getProductName());
                }
            }
        }
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


//    /**
//     * 根据码号查询产品的物流信息
//     */
//    @PostMapping(value = "/getFlowListByCode")
//    public AjaxResult getFlowListByCode(@RequestParam("code") String code) {
//        try {
//            List<List<Map<String, Object>>> lists = new ArrayList<List<Map<String, Object>>>();
//            List<Map<String, Object>> flowList = scanRecordService.getFlowListByCode(CodeRuleUtils.getCompanyIdByCode(code.trim()), code.trim());
//            if (flowList.size() > 0) {
//                for (int i = 0; i < flowList.size(); i++) {
//                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//                    Map<String, Object> frommap = new HashMap<String, Object>();
//                    Map<String, Object> inmap = new HashMap<String, Object>();
//                    Map<String, Object> map = flowList.get(i);
//                    if (map.get("title_name").equals("入库")) {
//                        if (map.get("storage_to") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
//                            if (sysDept != null) {
//                                boolean status = sysDept.getProvince().contains("市");
//                                if (status) {
//                                    frommap.put("name", sysDept.getProvince());
//                                    inmap.put("name", sysDept.getProvince());
//                                    inmap.put("value", 1);
//                                    list.add(frommap);
//                                    list.add(inmap);
//                                } else {
//                                    frommap.put("name", sysDept.getCity());
//                                    inmap.put("name", sysDept.getCity());
//                                    inmap.put("value", 1);
//                                    list.add(frommap);
//                                    list.add(inmap);
//                                }
//                            }
//                        }
//                        lists.add(list);
//                    } else {
//                        if (map.get("storage_from_id") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_from_id"))));
//                            if (sysDept != null) {
//                                if (sysDept.getProvince() != null) {
//                                    boolean status = sysDept.getProvince().contains("市");
//                                    if (status) {
//                                        frommap.put("name", sysDept.getProvince());
//                                        list.add(frommap);
//                                    } else {
//                                        frommap.put("name", sysDept.getCity());
//                                        list.add(frommap);
//                                    }
//                                }
//                            }
//                        }
//                        if (map.get("storage_to") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
//                            if (sysDept != null) {
//                                if (sysDept.getProvince() != null) {
//                                    boolean status = sysDept.getProvince().contains("市");
//                                    if (status) {
//                                        inmap.put("name", sysDept.getProvince());
//                                        inmap.put("value", 1);
//                                        list.add(inmap);
//                                    } else {
//                                        inmap.put("name", sysDept.getCity());
//                                        inmap.put("value", 1);
//                                        list.add(inmap);
//                                    }
//                                }
//                            }
//                        }
//                        lists.add(list);
//                    }
//                }
//            }
//            System.out.println(lists);
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("lists", lists);
//            logger.info("the method getInfoByKey end, result is {}", ajax);
//            return ajax;
//        } catch (Exception e) {
//            System.out.println(e);
//            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
//            logger.info("the method getInfoByKey end, result is {}", ajax);
//            return ajax;
//        }
//    }
//
//
//
//
//
//    /**
//     * 根据码号查询产品的物流信息
//     */
//    @PostMapping(value = "/getFlowListByCode1")
//    public AjaxResult getFlowListByCode1(@RequestParam("code") String code) {
//        try {
//            List<List<Map<String, String>>> lists = new ArrayList<List<Map<String, String>>>();
//            List<Map<String, Object>> flowList = scanRecordService.getFlowListByCode(CodeRuleUtils.getCompanyIdByCode(code.trim()), code.trim());
//            if (flowList.size() > 0) {
//                for (int i = 0; i < flowList.size(); i++) {
//                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//                    Map<String, String> frommap = new HashMap<String, String>();
//                    Map<String, String> inmap = new HashMap<String, String>();
//                    Map<String, Object> map = flowList.get(i);
//                    if (map.get("title_name").equals("入库")) {
//                        if (map.get("storage_to") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
//                            if (sysDept != null) {
//                                boolean status = sysDept.getProvince().contains("市");
//                                if (status) {
//                                    frommap.put("name", sysDept.getProvince());
//                                    inmap.put("name", sysDept.getProvince());
//                                    inmap.put("value", "1");
//                                    list.add(frommap);
//                                    list.add(inmap);
//                                } else {
//                                    frommap.put("name", sysDept.getCity());
//                                    inmap.put("name", sysDept.getCity());
//                                    inmap.put("value", "1");
//                                    list.add(frommap);
//                                    list.add(inmap);
//                                }
//                            }
//                        }
//                        lists.add(list);
//                    } else {
//                        if (map.get("storage_from_id") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_from_id"))));
//                            if (sysDept != null) {
//                                if (sysDept.getProvince() != null) {
//                                    boolean status = sysDept.getProvince().contains("市");
//                                    if (status) {
//                                        frommap.put("name", sysDept.getProvince());
//                                        list.add(frommap);
//                                    } else {
//                                        frommap.put("name", sysDept.getCity());
//                                        list.add(frommap);
//                                    }
//                                }
//                            }
//                        }
//                        if (map.get("storage_to") != null) {
//                            SysDept sysDept = deptService.selectDeptById(Long.valueOf(String.valueOf(map.get("storage_to"))));
//                            if (sysDept != null) {
//                                if (sysDept.getProvince() != null) {
//                                    boolean status = sysDept.getProvince().contains("市");
//                                    if (status) {
//                                        inmap.put("name", sysDept.getProvince());
//                                        inmap.put("value", "1");
//                                        list.add(inmap);
//                                    } else {
//                                        inmap.put("name", sysDept.getCity());
//                                        inmap.put("value", "1");
//                                        list.add(inmap);
//                                    }
//                                }
//                            }
//                        }
//                        lists.add(list);
//                    }
//                }
//            }
//            System.out.println(lists);
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("lists", lists);
//            logger.info("the method getInfoByKey end, result is {}", ajax);
//            return ajax;
//        } catch (Exception e) {
//            System.out.println(e);
//            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
//            logger.info("the method getInfoByKey end, result is {}", ajax);
//            return ajax;
//        }
//    }



    @GetMapping("/getScanRecordXx")
    public AjaxResult getScanRecordXx(ScanRecord scanRecord) {
        logger.info("the method getInfoByKey enter, param is {}", scanRecord);
        try {
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                scanRecord.setCompanyId(company_id);
            }
            //查询热力图数据
            List<Map<String, Object>> list = scanRecordService.getScanRecordXx(scanRecord);
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String, Object> mapinfo = list.get(i);
                    map = new HashMap<String, Object>();
                    map.put("id",mapinfo.get("id"));
                    map.put("name",mapinfo.get("product_name"));
                    //弹框的数据
                    JSONArray descList = new JSONArray();
                    JSONObject descObject = new JSONObject();
                    descObject.put("name","标识信息");
                    descObject.put("value",mapinfo.get("code"));
                    descList.add(descObject);
                    descObject = new JSONObject();
                    descObject.put("name","用户昵称");
                    descObject.put("value",mapinfo.get("terminal"));
                    descList.add(descObject);
                    descObject = new JSONObject();
                    descObject.put("name","扫码时间");
                    descObject.put("value",mapinfo.get("create_time").toString());
                    descList.add(descObject);
                    map.put("desc", JSON.toJSONString(descList));
                    if(mapinfo.get("photo")!=null){
                        String str[] = mapinfo.get("photo").toString().split(",");
                        List<String> list1 = Arrays.asList(str);
                        map.put("img",list1.get(0));
                    }else{
                        map.put("img",AccConstants.DEFAULT_IMAGE);
                    }
                    map.put("lon",mapinfo.get("longitude"));
                    map.put("lat",mapinfo.get("latitude"));
                    lists.add(map);
                }
            }
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




    @GetMapping("/updateScanRecordPrint")
    public AjaxResult updateScanRecordPrint(ScanRecord scanRecord) {
        logger.info("the method getInfoByKey enter, param is {}", scanRecord);
        try {
            //查询热力图数据
            List<Map<String, Object>> list = scanRecordService.getScanRecordXx(scanRecord);
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String, Object> mapinfo = list.get(i);
                    if(mapinfo.get("code")!=null){
                        Map<String,Object> smxxMap =  scanRecordService.getScanRecordByCode(CodeRuleUtils.getCompanyIdByCode(mapinfo.get("code").toString().trim()), mapinfo.get("code").toString().trim());
                        if(smxxMap!=null){
                            ScanRecord scanRecordUpdate = new ScanRecord();
                            scanRecordUpdate.setCode(mapinfo.get("code").toString());
                            scanRecordUpdate.setProductId(Long.valueOf(smxxMap.get("productId").toString()));
                            scanRecordUpdate.setProductName(smxxMap.get("productName").toString());
                            scanRecordService.updateScanRecordByCode(scanRecordUpdate);
                        }
                    }
                }
            }
            AjaxResult ajax = AjaxResult.success();
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }


}
