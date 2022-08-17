package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：      订单后台管理Controller
 */
@RestController
public class OrderAdminController {

    @Autowired
    OrderService orderService;

    //后台管理：订单列表
    @GetMapping("/admin/order/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){

        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }


    /**
     * 后台管理：订单发货
     * 发货。订单状态流程：0用户已取消；10未付款；20已付款；30已发货；40交易完成
     * @return
     */
    @PostMapping("/admin/order/delivered")
    public ApiRestResponse delivered(@RequestParam String orderNo){
        orderService.delivered(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 订单完结
     * 管理员和用户都可以调用
     * 发货。订单状态流程：0用户已取消；10未付款；20已付款；30已发货；40交易完成
     * @return
     */
    @PostMapping("/order/finish")
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

}
