package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository    //告诉IDE（此处是IDEA），表示这是一个资源
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    //list
    List<CartVO> selectList(@Param("userId") Integer userId);

    //add
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);


    //选中/不选中购物车的某个商品
    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId,@Param("selected") Integer selected);


}