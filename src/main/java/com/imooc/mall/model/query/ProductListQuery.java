package com.imooc.mall.model.query;

import java.util.List;

/**
 * 描述：      查询商品列表的Query
 * 在ProductServiceImpl中用到：前台商品模块--商品搜索、商品排序、商品列表、目录展示
 * public PageInfo list(ProductListReq productListReq){
 * //构建Query对象
 * ProductListQuery productListQuery = new ProductListQuery();  ...}
 */
public class ProductListQuery {
    private String keyword;

    private List<Integer> categoryIds;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
