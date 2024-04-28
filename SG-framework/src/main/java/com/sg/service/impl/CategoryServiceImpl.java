package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.ResponseResult;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Article;
import com.sg.domain.entity.Category;
import com.sg.domain.vo.CategoryVo;
import com.sg.domain.vo.PageVo;
import com.sg.mapper.CategoryMapper;
import com.sg.service.ArticleService;
import com.sg.service.CategoryService;
import com.sg.util.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author QiHang777
 * @since 2023-03-27 23:05:38
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        queryWrapper.select(Category::getId,Category::getDescription,Category::getName,Category::getStatus);
        List<Category> list = list(queryWrapper);
        return BeanCopyUtils.copyBeanList(list, CategoryVo.class);
    }

    @Override
    public PageVo listAll(Integer pageNum, Integer pageSize, CategoryVo categoryVo) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(categoryVo)){
            queryWrapper.like(StringUtils.hasText(categoryVo.getName()),Category::getName,categoryVo.getName());
            queryWrapper.eq(StringUtils.hasText(categoryVo.getStatus()),Category::getStatus,categoryVo.getStatus());
        }

        //分页查询查的字段是否多了，顺便也看看其它分页查询
        Page<Category> page = new Page<>(pageNum, pageSize);
        Page<Category> categoryPage = page(page, queryWrapper);
        PageVo pageVo = new PageVo(categoryPage.getRecords(), categoryPage.getTotal());
        return pageVo;
    }

}

