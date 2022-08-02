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
 * 部门信息
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
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
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
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
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
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增'" + dept.getDeptName() + "'失败，名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());

        int addres = (int) deptService.insertDept(dept);
        if (addres > 0) {
            //判断新增dept是不是企业级别
            if (dept.getParentId() == AccConstants.ADMIN_DEPT_ID) {
                long companyId = addres;

                //判断该企业对应的分表是否创建
                int res = -1;
                //1.t_code
                if (!codeService.checkCompanyTableExist(companyId, "t_code_")) {
                    String sql = "CREATE TABLE t_code_" + companyId + "(\n" +
                            "    code_index BIGINT NOT NULL AUTO_INCREMENT  COMMENT '流水号' ,\n" +
                            "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                            "    status INT    COMMENT '状态' ,\n" +
                            "    code VARCHAR(64)    COMMENT '码' ,\n" +
                            "    code_acc VARCHAR(64)    COMMENT '防窜码' ,\n" +
                            "    code_type VARCHAR(64)    COMMENT '码类型（箱码or单码）' ,\n" +
                            "    p_code VARCHAR(64)    COMMENT '所属箱码' ,\n" +
                            "    code_attr_id BIGINT    COMMENT '码属性id' ,\n" +
                            "    PRIMARY KEY (code_index)\n" +
                            ") COMMENT = '码表 ';";
                    res = createTableByCompany(sql);
                }

                //2.码流转明细表
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_in_code_flow_")) {
                    String sql = "CREATE TABLE t_in_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                            "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                            "    create_user BIGINT    COMMENT '创建人' ,\n" +
                            "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '单码流转记录表 ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_out_code_flow_")) {
                    String sql = "CREATE TABLE t_out_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                            "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                            "    create_user BIGINT    COMMENT '创建人' ,\n" +
                            "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '单码流转记录表 ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_transfer_code_flow_")) {
                    String sql = "CREATE TABLE t_transfer_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                            "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                            "    create_user BIGINT    COMMENT '创建人' ,\n" +
                            "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '单码流转记录表 ';";
                    res = createTableByCompany(sql);
                }
                if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_back_code_flow_")) {
                    String sql = "CREATE TABLE t_back_code_flow_" + companyId + "(\n" +
                            "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                            "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                            "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                            "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                            "    create_user BIGINT    COMMENT '创建人' ,\n" +
                            "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                            "    PRIMARY KEY (id)\n" +
                            ") COMMENT = '单码流转记录表 ';";
                    res = createTableByCompany(sql);
                }

                if (res != 0) {
                    return AjaxResult.error("新增企业'" + dept.getDeptName() + "'失败，数据分片异常！");
                }
            }
        }
        return toAjax(addres);
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 按企业创建分表
     *
     * @param sql 建表sql
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
     * 查询企业库存
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
     * 导出经销商记录
     */
    @Log(title = "经销商记录", businessType = BusinessType.EXPORT)
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
        String fileName = util.exportExcel(list, "-"+ DateUtils.getDate()+"经销商信息").get("msg").toString();

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
     * 导出经销商记录
     */
    @Log(title = "经销商记录", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadStock")
    public void downloadStock( Long tenantId, HttpServletResponse response)
    {
        ProductStock productStock = new ProductStock();
        productStock.setTenantId(tenantId);
        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        ExcelUtil<ProductStock> util = new ExcelUtil<ProductStock>(ProductStock.class);
        String fileName = util.exportExcel(list, "-"+ DateUtils.getDate()+"库存信息").get("msg").toString();

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

}
