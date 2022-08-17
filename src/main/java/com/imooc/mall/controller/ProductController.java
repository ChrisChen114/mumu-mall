package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：      前台商品ProductController
 */
@RestController
public class ProductController {
    //在controller中区分给前台用户用的controller与给管理员用的AdminController
    //但是在ProductService可以两者共用，复用一些方法
    @Autowired
    ProductService productService;

    @ApiOperation("前台商品详情")
    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    /**
     * 前台商品列表
     * 功能包括：商品搜索、商品排序、商品列表、目录展示
     * @param productListReq
     * @return
     */
    @ApiOperation("前台商品列表")
    @GetMapping("/product/list")
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo pageInfo = productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }

}
