package com.ztl.gym.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ztl.gym.common.config.RuoYiConfig;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.Constants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CommonUtil;
import com.ztl.gym.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.file.FileUploadUtils;
import com.ztl.gym.common.utils.file.FileUtils;
import com.ztl.gym.framework.config.ServerConfig;

import java.util.Map;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
public class CommonController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private CommonService commonService;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @CrossOrigin
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            /*// 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;*/

            String url = CommonUtil.uploadPic(file, Constants.IMG);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", file.getName());
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @CrossOrigin
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 查询下级经销商用户列表【自营用户】
     */

    @GetMapping("/common/getTenantByParent")
    public AjaxResult getTenantByParent() {
        return AjaxResult.success(commonService.getTenantByParent());
    }

    @GetMapping("/common/getStorageNo/{i}")
    public AjaxResult getStorageNo(@PathVariable int i) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", commonService.getStorageNo(i));
        return ajax;
    }

}
