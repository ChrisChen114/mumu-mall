package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：    用户控制器
 * 用户模块：注册新用户、登录、更新个性签名、退出登录、管理员登录
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody   //由于采用json格式的通信方式，所以ResponseBody用于返回json格式的内容
    public User personalPage() {
        return userService.getUser();
    }



    /**
     * 用户模块：注册新用户
     * 如果只单纯的查询，用get请求
     * 如果涉及到写入操作，使用post请求
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ImoocMallException {
        //null 或 ""
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        //检查密码长度,密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }

        try {
            userService.register(userName, password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return ApiRestResponse.success();
    }

    /**
     * 用户模块：登录
     * 登录状态需要保持（使用HttpSession技术），登录后，会保存用户信息到session
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException {
        //null 或 ""
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(userName, password);
        //保存用户信息到session时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.IMOOC_MALL_USER,user);
        return ApiRestResponse.success(user);
    }

    /**
     * 用户模块：更新个性签名
     * @param session
     * @param signature
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody                         //RequestParam用于获取参数
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws ImoocMallException {
        //从session里获取参数
        User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        //获取Id，用于确定是哪个user需更新
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInfomation(user);
        return ApiRestResponse.success();
    }

    /**
     * 用户模块：登出;退出登录，重点是清除session
     * 从session中清除登录信息
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestResponse.success();
    }


    /**
     * 用户模块：管理员登录
     * 登录状态需要保持，登录后，会保存用户信息到session
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException {
        //null 或 ""
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(userName, password);
        //根据角色role
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.IMOOC_MALL_USER,user);
            return ApiRestResponse.success(user);
        }else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }

    }


}
