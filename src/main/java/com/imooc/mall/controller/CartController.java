package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：      购物车Controller
 */
@RestController   //RestController =Controller + ResponseBody,记住
@RequestMapping("/cart")   //增加前缀
public class CartController {

    @Autowired
    CartService cartService;


    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list(){
        //内部获取用户ID，防止横向越权
        // （note：横向越权：用户与用户之间的越权；纵向越权：普通用户处理管理员的权限）
        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);
    }

    /**
     * 前台： 添加商品到购物车
     * 两个参数放在URI中的，使用@RequestParam注解
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam  Integer productId,@RequestParam Integer count){
        //内部获取用户ID，防止横向越权
        //先验证登录（UserFilter.currentUser.getId()），在添加购物车
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }


    /**
     * 更新购物车某个商品的数量
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(@RequestParam  Integer productId,@RequestParam Integer count){
        //先验证登录，在添加购物车
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车的某个商品")
    public ApiRestResponse delete(@RequestParam  Integer productId){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }


    @PostMapping("/select")
    @ApiOperation("选中/不选中购物车的某个商品")
    public ApiRestResponse select(@RequestParam  Integer productId,@RequestParam  Integer selected){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId,selected);
        return ApiRestResponse.success(cartVOList);
    }


    @PostMapping("/selectAll")
    @ApiOperation("全选/全不选中购物车的所有商品")
    public ApiRestResponse selectAll(@RequestParam  Integer selected){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }




}
