package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 提供给后台管理员 新增商品 用
 * @PostMapping("/admin/product/add")
 * public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq)
 */
public class AddProductReq {

    //校验常见的功能
    //@NotNull(message = "商品名称不能为null")
    //@Min(1)
    //@Max(10000)

    @NotNull(message = "商品名称不能为null")
    private String name;
    @NotNull(message = "商品图片不能为null")
    private String image;

    private String detail;

    @NotNull(message = "商品分类不能为null")
    private Integer categoryId;


    @NotNull(message = "商品价格不能为null")
    @Min(value = 1, message = "商品价格不能小于1分钱")   //最小为1分钱
    private Integer price;

    @NotNull(message = "商品库存不能为null")
    @Max(value = 10000,message = "库存不能大于10000")   //库存最大为10000
    private Integer stock;

    private Integer status;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddProductReq{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}