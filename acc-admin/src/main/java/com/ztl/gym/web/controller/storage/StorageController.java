package com.ztl.gym.web.controller.storage;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.web.controller.payment.PaymentRecordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库Controller
 *
 * @author zhucl
 * @date 2021-04-13
 */
@RestController
@RequestMapping("/storage/storage")
public class StorageController extends BaseController {
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(StorageController.class);
    @Autowired
    private IStorageService storageService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeService codeService;
    private static final String COMMA = ",";

    /**
     * 查询仓库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(Storage storage) {
        startPage();
        List<Storage> list = storageService.selectStorageList(storage);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:export')")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Storage storage) {
        List<Storage> list = storageService.selectStorageList(storage);
        ExcelUtil<Storage> util = new ExcelUtil<Storage>(Storage.class);
        return util.exportExcel(list, "storage");
    }

    /**
     * 获取仓库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storageService.selectStorageById(id));
    }

    /**
     * 新增仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:add')")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Storage storage) {
        return toAjax(storageService.insertStorage(storage));
    }

    /**
     * 修改仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:edit')")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Storage storage) {
        return toAjax(storageService.updateStorage(storage));
    }

    /**
     * 删除仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:remove')")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageService.deleteStorageByIds(ids));
    }

    /**
     * 获取用户仓库信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:listByUser')")
    @GetMapping(value = "/listStorageByUser")
    public AjaxResult getUserStotageList() {
        Storage storage = new Storage();
        storage.setStatus(0L);
        if (!SecurityUtils.getLoginUserCompany().getDeptId().equals(AccConstants.ADMIN_DEPT_ID)) {
            //判断用户是企业还是经销商
            String[] ancestors = SecurityUtils.getLoginUserCompany().getAncestors().split(",");
            if (ancestors.length > 2) {
                storage.setLevel(AccConstants.STORAGE_LEVEL_TENANT);
                storage.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
            } else {
                storage.setLevel(AccConstants.STORAGE_LEVEL_COMPANY);
                storage.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            }
        }
        List<Storage> list = storageService.selectStorageByUser(storage);
        return AjaxResult.success(list);
    }

    /**
     * 根据tenant获取用户仓库信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:listByTenant')")
    @GetMapping(value = "/getStoragesByTenant/{tenantId}")
    public AjaxResult getStoragesByTenant(@PathVariable("tenantId") Long tenantId) {
        Storage storage = new Storage();
        storage.setStatus(0L);
        if (tenantId != null && tenantId > 0) {
            storage.setTenantId(tenantId);
        }
        List<Storage> list = storageService.selectStorageByUser(storage);
        return AjaxResult.success(list);
    }

    /**
     * 根据码号查询相关产品和码信息
     */
    @GetMapping(value = "/getRecordByCode")
    public AjaxResult getRecordByCode(@RequestParam("storageType") Integer storageType, @RequestParam("code") String code) {
        long companyId = 0;
        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        //适配出库入库批量操作，如果code值用英文逗号隔开为批量操作,相同code值去重操作
        List<String> codeList = new LinkedList<>();
        if (code != null && code.contains(COMMA)) {
            logger.info("多码入库流程");
            codeList = Arrays.stream(code.split(COMMA)).filter(item -> StringUtils.isNotEmpty(item)).distinct().collect(Collectors.toList());
        } else {
            logger.info("单码入库流程");
            codeList.add(code);
        }
        //企业入库单号
        String inNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_IN);
        StorageVo storageVo = null;
        List<StorageVo> storageVos = new LinkedList<>();
        for (String str : codeList) {
            if (commonService.judgeStorageIsIllegalByValue(companyId, storageType, str)) {
                //pc端暂时不做对未关联的产品的code做赋值，直接返回该产品未赋值
                storageVo = storageService.selectLastStorageByCode(str);
                if (Objects.isNull(storageVo.getProductId())) {
                    logger.error("该{}码尚未赋值，请先赋值产品信息！", storageVo.getCode());
                    return AjaxResult.error("该" + storageVo.getCode() + "码尚未赋值，请先赋值产品信息！");
                }
                storageVo.setInNo(inNo);
                storageVos.add(storageVo);
            } else {
                return AjaxResult.error("批量码信息中的不正确：请检查" + str + "的有效性");
            }
        }
        //判断是否是同一个产品
        List<Long> productId = storageVos.stream().map(StorageVo::getProductId).distinct().collect(Collectors.toList());
        if (productId.size() > 1) {
            return AjaxResult.error(" 码中有不同类型的产品：请检查货码的产品信息");
        }
        //批量入库时，筛选出一个箱子里的单码剔除，界面只展示箱码
        return AjaxResult.success(storageService.filterDuplicateSingleCode(storageVos));
    }

    /**
     * 查询流转单的码明细列表
     *
     * @param storageType
     * @param storageRecordId
     * @return
     */
    @GetMapping("/listCode")
    public TableDataInfo listCode(int storageType, long storageRecordId) {
        try {
            if(storageType==3){
                storageType = 2;
            }
            List<String> codeStrs = commonService.selectCodeByStorageForPage(SecurityUtils.getLoginUserTopCompanyId(), storageType, storageRecordId);
            Map<String,Object> codeParam = new HashMap<>();
            codeParam.put("companyId",SecurityUtils.getLoginUserTopCompanyId());
            codeParam.put("codes",codeStrs);

            startPage();
            List<Code> lists= codeService.selectCodes(codeParam);
            if(lists.size()==0){
                throw new CustomException("请扫码确认物流状态！");
            }
            return getDataTable(lists);
        }catch (Exception e){
            throw new CustomException("未查询到相关码信息或未扫码确认物流状态");
        }

    }
}
