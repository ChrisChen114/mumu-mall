package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //新增
    Product selectByName(String name);

    //新增
    int batchUpdateSellStatus(@Param("ids") Integer[] ids,@Param("sellStatus") Integer sellStatus);

    //新增
    List<Product> selectListForAdmin();

    //新增
    List<Product> selectList(@Param("query") ProductListQuery query);


}