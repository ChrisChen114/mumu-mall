package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //新增
    Category selectByName(String name);

    //后台管理：分页的查询
    List<Category> selectList();

    //前台管理：分页的查询
    List<Category> selectCategoriesByParentId(Integer parentId);
}