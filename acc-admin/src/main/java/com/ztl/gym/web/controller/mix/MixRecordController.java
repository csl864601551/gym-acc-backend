package com.ztl.gym.web.controller.mix;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.config.RuoYiConfig;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.file.FileUtils;
import com.ztl.gym.common.utils.ip.IpUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.mix.domain.MixRecord;
import com.ztl.gym.mix.domain.vo.MixRecordVo;
import com.ztl.gym.mix.service.IMixRecordService;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import com.ztl.gym.web.controller.code.CodeRecordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    private static final Logger log = LoggerFactory.getLogger(MixRecordController.class);

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
    @CrossOrigin
    @GetMapping("/download")
    public void download(String productName, String productNo, String mixType, long companyId, HttpServletResponse response)
    {
        MixRecord mixRecord = new MixRecord();
        if(!productName.equals("null") && productName != "") {
            mixRecord.setProductName(productName);
        }
        if(!productNo.equals("null") && productNo != "") {
            mixRecord.setProductNo(productNo);
        }
        if(!mixType.equals("null") && mixType != "") {
            mixRecord.setMixType(Integer.parseInt(mixType));
        }
        mixRecord.setCompanyId(companyId);
        List<MixRecordVo> list = mixRecordService.selectMixRecordExport(mixRecord);
        ExcelUtil<MixRecordVo> util = new ExcelUtil<MixRecordVo>(MixRecordVo.class);
        String fileName = util.exportExcel(list, "-"+ DateUtils.getDate()+"窜货记录").get("msg").toString();

        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
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
