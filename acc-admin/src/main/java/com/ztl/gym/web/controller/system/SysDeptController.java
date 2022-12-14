package com.ztl.gym.web.controller.system;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.CodeTestService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.config.RuoYiConfig;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.vo.SysDeptVo;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.file.FileUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.framework.web.domain.server.Sys;
import com.ztl.gym.mix.domain.MixRecord;
import com.ztl.gym.mix.domain.vo.MixRecordVo;
import com.ztl.gym.product.domain.ProductStock;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.system.domain.SysDeptERP;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.web.controller.area.CompanyAreaController;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ztl.gym.common.constant.UserConstants;

import javax.servlet.http.HttpServletResponse;

/**
 * ????????????
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SysDeptController.class);

    @Autowired
    private DruidDataSource dataSource;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private CodeTestService codeService;

    @Autowired
    private IProductStockService productStockService;

    /**
     * ??????????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * ????????????????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * ????????????????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * ???????????????????????????
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * ?????????????????????????????????
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * ????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("??????'" + dept.getDeptName() + "'????????????????????????");
        }
        dept.setCreateBy(SecurityUtils.getUsername());

        int addres = (int) deptService.insertDept(dept);
        if (addres > 0) {
            //????????????dept?????????????????????
            if (dept.getParentId() == AccConstants.ADMIN_DEPT_ID) {
                long companyId = addres;

                //??????????????????????????????????????????
                int res = -1;
                //1.t_code
                if (!codeService.checkCompanyTableExist(companyId, "t_code_")) {
                    String sql = "CREATE TABLE t_code_" + companyId + "(\n" +
                            "    code_index BIGINT NOT NULL AUTO_INCREMENT  COMMENT '?????????' ,\n" +
                            "    company_id BIGINT    COMMENT '??????ID' ,\n" +
                            "    status INT    COMMENT '??????' ,\n" +
                            "    code VARCHAR(64)    COMMENT '???' ,\n" +
                            "    code_acc VARCHAR(64)    COMMENT '?????????' ,\n" +
                            "    code_type VARCHAR(64)    COMMENT '??????????????????or?????????' ,\n" +
                            "    p_code VARCHAR(64)    COMMENT '????????????' ,\n" +
                            "    code_attr_id BIGINT    COMMENT '?????????id' ,\n" +
                            "    PRIMARY KEY (code_index)\n" +
                            ") COMMENT = '?????? ';";
                    res = createTableByCompany(sql);
                }

                //2.??????????????????
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_in_code_flow_")) {
                    String sql = "CREATE TABLE t_in_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '??????ID' ,\n" +
                            "    company_id BIGINT    COMMENT '??????ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '??????' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '????????????id' ,\n" +
                            "    create_user BIGINT    COMMENT '?????????' ,\n" +
                            "    create_time DATETIME    COMMENT '????????????' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '????????????????????? ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_out_code_flow_")) {
                    String sql = "CREATE TABLE t_out_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '??????ID' ,\n" +
                            "    company_id BIGINT    COMMENT '??????ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '??????' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '????????????id' ,\n" +
                            "    create_user BIGINT    COMMENT '?????????' ,\n" +
                            "    create_time DATETIME    COMMENT '????????????' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '????????????????????? ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_transfer_code_flow_")) {
                    String sql = "CREATE TABLE t_transfer_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '??????ID' ,\n" +
                            "    company_id BIGINT    COMMENT '??????ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '??????' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '????????????id' ,\n" +
                            "    create_user BIGINT    COMMENT '?????????' ,\n" +
                            "    create_time DATETIME    COMMENT '????????????' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '????????????????????? ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_back_code_flow_")) {
                    String sql = "CREATE TABLE t_back_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '??????ID' ,\n" +
                            "    company_id BIGINT    COMMENT '??????ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '??????' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '????????????id' ,\n" +
                            "    create_user BIGINT    COMMENT '?????????' ,\n" +
                            "    create_time DATETIME    COMMENT '????????????' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '????????????????????? ';";
                    res = createTableByCompany(sql);
                }

                if (res != 0) {
                    return AjaxResult.error("????????????'" + dept.getDeptName() + "'??????????????????????????????");
                }
            }
        }
        return toAjax(addres);
    }

    /**
     * ????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("????????????'" + dept.getDeptName() + "'??????????????????????????????");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error("????????????'" + dept.getDeptName() + "'????????????????????????????????????");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return AjaxResult.error("???????????????????????????????????????");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * ????????????
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("??????????????????,???????????????");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("??????????????????,???????????????");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * ?????????????????????
     *
     * @param sql ??????sql
     * @return
     */
    private int createTableByCompany(String sql) {
        int res = -1;
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            res = statement.executeUpdate(sql);
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            res = -2;
        } finally {
            return res;
        }
    }

    /**
     * ??????????????????
     *
     * @param dept
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:listStock')")
    @GetMapping("/listStock")
    public TableDataInfo listStock(SysDept dept) {
        startPage();
        ProductStock productStock = new ProductStock();
        productStock.setTenantId(dept.getDeptId());
        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        return getDataTable(list);
    }

    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public void download(String deptName, String status, Long companyId, HttpServletResponse response)
    {
        SysDept dept = new SysDept();
        if(!deptName.equals("null") && deptName !="" && !deptName.equals("undefined")) {
            dept.setDeptName(deptName);
        }
        if(!status.equals("null") && status !="" && !status.equals("undefined")) {
            dept.setStatus(status);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dataScope","AND (d.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = " + companyId + " or find_in_set( " + companyId + " , ancestors ) ))");
        dept.setParams(params);
        List<SysDeptVo> list = deptService.selectDeptExport(dept);
        ExcelUtil<SysDeptVo> util = new ExcelUtil<SysDeptVo>(SysDeptVo.class);
        String fileName = util.exportExcel(list, "-"+ DateUtils.getDate()+"???????????????").get("msg").toString();

        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("????????????({})??????????????????????????? ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("??????????????????", e);
        }
    }
    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadStock")
    public void downloadStock( Long tenantId, HttpServletResponse response)
    {
        ProductStock productStock = new ProductStock();
        productStock.setTenantId(tenantId);
        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        ExcelUtil<ProductStock> util = new ExcelUtil<ProductStock>(ProductStock.class);
        String fileName = util.exportExcel(list, "-"+ DateUtils.getDate()+"????????????").get("msg").toString();

        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("????????????({})??????????????????????????? ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("??????????????????", e);
        }
    }

}
