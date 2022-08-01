package com.ztl.gym.web.controller.open.product;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open/product")
public class OpenProductController {

    @Autowired
    private IProductService productService;

    /**
     * 查询所有产品
     *
     * @return
     */
    @GetMapping("/listProduct")
    public AjaxResult listProduct() {
        Product product = new Product();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            product.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<Product> productList = productService.selectTProductList(product);
        return AjaxResult.success(productList);
    }

    /**
     * 新增产品
     */
    @Log(title = "ERP同步产品", businessType = BusinessType.INSERT)
    @PostMapping("getERPProduct")
    public AjaxResult getERPProduct(@RequestBody Product product) {
        //初始化数据
        if (StrUtil.isEmpty(product.getProductName())) {
            throw new CustomException("物料名称不能为空！", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(product.getProductNo())) {
            throw new CustomException("规格型号不能为空！", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(product.getBarCode())) {
            throw new CustomException("物料编码不能为空！", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(product.getPrintNo())) {
            throw new CustomException("打印型号不能为空！", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(product.getBoxCount())) {
            throw new CustomException("包装规格不能为空！", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(product.getProductSeries())) {
            throw new CustomException("产品系列不能为空！", HttpStatus.ERROR);
        }
        if (product.getAttributeList() == null || product.getAttributeList().size() == 0) {
            throw new CustomException("外箱标签打标属性不能为空！", HttpStatus.ERROR);
        }

        product.setCategoryOne(30L);
        product.setCategoryTwo(68L);
        Product productQuery = new Product();
        productQuery.setBarCode(product.getBarCode());
        List<Product> list = productService.selectTProductList1(productQuery);
        if (list.size() > 0) {
            product.setId(list.get(0).getId());
            productService.updateTProduct(product);
        } else {
            productService.insertTProduct(product);
        }
        return AjaxResult.success("同步成功！");
    }
}
