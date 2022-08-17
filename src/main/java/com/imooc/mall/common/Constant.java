package com.imooc.mall.common;

import com.google.common.collect.Sets;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 描述：常量值
 */
@Component
public class Constant {
    public static final String SALT = "vnaboir4451234yy";

    public static final String IMOOC_MALL_USER = "imooc_mall_user";


    public static String FILE_UPLOAD_DIR;

    //用这个方法实现静态变量的赋值
    //上面还需要增加一个注解 @Component
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }


    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    //枚举：商品上下架状态枚举
    public interface SaleStatus {
        int NOT_SALE = 0;//商品下架状态
        int SALE = 1;//商品上架状态
    }


    //枚举：商品是否被选择的枚举
    public interface Cart {
        int UN_CHECKED = 0;//购物车未被选中状态
        int CHECKED = 1;//购物车被选中状态
    }

    //与订单状态有关的枚举
    public enum OrderStatusEnum{
        CANCELED(0,"用户已取消"),
        NOT_PAID(10,"未付款"),
        PAID(20,"已付款"),
        DELIVERED(30,"已发货"),
        FINISHED(40,"交易完成");
        private String value;
        private int code;

        OrderStatusEnum(int code,String value ) {
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code){
            //这里的values()是指向枚举列表
            for (OrderStatusEnum orderStatusEnum:values()) {
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            //未找到则抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
