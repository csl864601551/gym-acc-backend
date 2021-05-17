package com.ztl.gym.web.controller.open.product;

import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("@ss.hasPermi('code:record:listProduct')")
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
}
