package com.ztl.gym.web.controller.storage;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库Controller
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/storage/in")
public class StorageInController extends BaseController {
    @Autowired
    private IStorageInService storageInService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private CommonService commonService;

    /**
     * 查询入库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:in:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageIn storageIn) {
        startPage();
        List<StorageIn> list = storageInService.selectStorageInList(storageIn);
        return getDataTable(list);
    }

    /**
     * 导出入库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:in:export')")
    @Log(title = "入库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageIn storageIn) {
        List<StorageIn> list = storageInService.selectStorageInList(storageIn);
        ExcelUtil<StorageIn> util = new ExcelUtil<StorageIn>(StorageIn.class);
        return util.exportExcel(list, "in");
    }

    /**
     * 获取入库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:in:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storageInService.selectStorageInById(id));
    }
    /**
     * 获取入库码产品详细信息
     */
    @GetMapping("/getCodeDetailById")
    public AjaxResult getCodeDetailById(@RequestParam("companyId") Long companyId, @RequestParam("id") Integer id) {
        return AjaxResult.success(storageInService.getCodeDetailById(companyId,id));
    }

    /**
     * 新增入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:add')")
    @Log(title = "入库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> map) {
        storageInService.insertStorageIn(map);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", map.get("id").toString());
        return ajax;
    }

    /**
     * 修改入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:edit')")
    @Log(title = "入库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageIn storageIn) {
        return toAjax(storageInService.updateStorageIn(storageIn));
    }

    /**
     * 删除入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:remove')")
    @Log(title = "入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageInService.deleteStorageInByIds(ids));
    }

    /**
     * updateInStatusByCode
     * PDA扫码入库
     */
    @Log(title = "PDA扫码入库", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/updateInStatusByCode")
    public AjaxResult updateInStatusByCode(@RequestBody Map<String, Object> map)
    {
        return toAjax(storageInService.updateInStatusByCode(map));
    }
    /**
     * updateTenantIn
     * PC经销商确认收货
     */
    @Log(title = "经销商确认收货", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/updateTenantIn")
    public AjaxResult updateTenantIn(@RequestBody Map<String, Object> map)
    {
        return toAjax(storageInService.updateTenantIn(map));
    }

    @Log(title = "入库管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long storageId) throws Exception {
        List<String> list = new ArrayList<String>();
        List<String> list1 = new ArrayList<String>();
        Map<String, Object> map = new HashMap<String,Object>();
        String codeone = null;
        ExcelUtil<Code> util = new ExcelUtil<Code>(Code.class);
        List<Code> codeList = util.importExcel(file.getInputStream());
        if(codeList.size()>0){
            for(Code codeinfo : codeList){
                if(codeinfo.getCodeTypeName().equals("箱码")){
                    String code = codeinfo.getCode();
                    String str1=code.substring(0, code.indexOf("="));
                    String str2=code.substring(str1.length()+1, code.length());
                    codeone = str2;
                    list.add(str2);
                }
//                if(codeinfo.getCodeTypeName().equals("单码")){
//                    String code = codeinfo.getCode();
//                    String str1=code.substring(0, code.indexOf("="));
//                    String str2=code.substring(str1.length()+1, code.length());
//                    codeone = str2;
//                    list1.add(str2);
//                }
            }
        }
        //根据code 获取信息
        long companyId = 0;
        Integer storageType = 1;
        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        StorageVo storageVo = new StorageVo();
        if (commonService.judgeStorageIsIllegalByValue(companyId, storageType, codeone)) {
            storageVo = storageService.selectLastStorageByCode(codeone);
        }
        if(storageVo!=null){
            map.put("inNo",storageVo.getInNo());
            map.put("productId",storageVo.getProductId());
            map.put("batchNo",storageVo.getBatchNo());
//            map.put("inNum",codeList.size()-list.size());
//            map.put("actInNum",codeList.size()-list.size());
            map.put("toStorageId",storageId);
            map.put("remark","");
            map.put("thirdPartyFlag","1");
        }
        map.put("codes",list);
        int show = storageInService.insertStorageIn(map);
        return AjaxResult.success(show);
    }
}
