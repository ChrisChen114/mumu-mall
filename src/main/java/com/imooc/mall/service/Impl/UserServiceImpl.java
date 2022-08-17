package com.imooc.mall.service.Impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 描述：          UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {
    //注入接口
    @Autowired
    UserMapper userMapper;

    /**
     * 通过主键来查询一个对象
     * @return
     */
    @Override
    public User getUser() {
        //通过主键来查询一个对象
        return userMapper.selectByPrimaryKey(1);
    }

    /**
     * @param userName
     * @param password
     */
    @Override
    public void register(String userName, String password) throws ImoocMallException, NoSuchAlgorithmException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //写到数据库
        User user = new User();
        user.setUsername(userName);
        //含盐值的MD5加密
        user.setPassword(MD5Utils.getMd5Str(password));
        //user.setPassword(password);
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 登录
     * 此方法并没有在UserService接口中定义方法，而是先在Impl类中先写，然后在Override上右键选择 pull method 'login' to 'UserService'
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    @Override
    public User login(String userName, String password) throws ImoocMallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMd5Str(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //传入md5Password，然后在select语句中进行比对
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null){
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;

    }

    /**
     * 更新个性签名
     * @param user
     */
    @Override
    public void updateInfomation(User user) throws ImoocMallException {
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>1){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }

    }


    /**
     * 角色检查-用于管理员的验证
     * @param user
     * @return
     */
    @Override
    public boolean checkAdminRole(User user){
        //1-普通用户，2-管理员
        return user.getRole().equals(2);
    }

}
