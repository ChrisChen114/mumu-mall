package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：      目录分类Service实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 后台管理：增加目录分类
     *
     * @param addCategoryReq
     */
    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        //这种拷贝太麻烦
        //category.setName(addCategoryReq.getName());
        //...余下几个set方法
        //小技巧，一下拷贝字段类型相同的所有字段
        BeanUtils.copyProperties(addCategoryReq, category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            //ImoocMallException 继承的父类改成了RuntimeException，原来是Exception
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }

    }


    /**
     * 后台管理：更新目录分类
     *
     * @param updateCategory
     */
    @Override
    public void update(Category updateCategory) {
        // 如Postman的body中输入这样的测试内容：    {"id":"29","name":"冰淇淋","type":3}
        //更新时候，名字不能与别人冲突
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            //名字不为空、ID不一样,此时需要给拒绝掉
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }


    /**
     * 后台管理：删除分类
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        //查不到记录，无法删除，删除失败
        if (categoryOld == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }

        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    /**
     * 后台管理：分类列表（平铺）
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        //利用PageHelper设置分页参数
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        //查询
        List<Category> categoryList = categoryMapper.selectList();
        //new一个PageInfo，返回给前端
        PageInfo pageInfo = new PageInfo<>(categoryList);
        //PageInfo里面会蕴藏着List<Category> categoryList内容
        return pageInfo;
    }

    @Override
    @Cacheable(value = "listCategoryForCustomer")   //springframe提供的
    public List<CategoryVO> listCategoryForCustomer(Integer parentId) {
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        //调用私有方法
        recursivelyFindCategories(categoryVOList, parentId);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
        //递归获取所有子类别，并组合成为一个“目录树”
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if ( !CollectionUtils.isEmpty(categoryList)) {
            //不等于空
            for (int i = 0; i < categoryList.size(); i++) {
                //父节点，将基本的字段进行拷贝                  赋值
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                //子节点，将private List<CategoryVO> childCategory 进行赋值
                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
            }

        }
    }

}
