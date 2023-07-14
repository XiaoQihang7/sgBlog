package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.ResponseResult;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Article;
import com.sg.domain.entity.ArticleTag;
import com.sg.domain.entity.Category;
import com.sg.domain.vo.*;
import com.sg.mapper.ArticleMapper;
import com.sg.service.ArticleService;
import com.sg.service.ArticleTagService;
import com.sg.service.CategoryService;
import com.sg.util.BeanCopyUtils;
import com.sg.util.RedisCache;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sg.domain.constants.SystemConstants.ARTICLE_STATUS_NORMAL;
import static com.sg.domain.constants.SystemConstants.LIU_LAN;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {

    @Autowired
    CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);

        //只查流量量最高的十条，使用分页完成
        Page<Article> articlePage = new Page<>(1, 10);
        page(articlePage,queryWrapper);
        List<Article> records = articlePage.getRecords();

        //bean拷贝，只展示标题和访问量
//        List<HotArticleVo> hotArticleVo = new ArrayList<>();
        List<HotArticleVo> hotArtVo = records.stream().map(r -> {
            HotArticleVo articleVo = new HotArticleVo();
            BeanUtils.copyProperties(r, articleVo);
//            hotArticleVo.add(articleVo);
            return articleVo;
        }).collect(Collectors.toList());

        return ResponseResult.okResult(hotArtVo);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize,
                                      @RequestParam (required = false)Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article ->
//                {article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
//                    return article;
//              }
                        //在实体类中进行加入的数据库中不存在的一个字段
                article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())
                )
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //【优化】：从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(LIU_LAN, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        //给旁边的标签赋予name，方便进行分类检索
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
        }
        articleDetailVo.setCategoryName(category.getName());
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


    @Override
    public ResponseResult updateViewCount(Long id) {
        //根据文章id，查询redis中是否存在该数据
        int cacheMapValue = (int) redisCache.getCacheMapValue(LIU_LAN, id.toString());
        //数据缓存不存在
        if (cacheMapValue==0){
            Article thisArticle = getById(id);
            HashMap<String, Integer> map = new HashMap<>();
            map.put(thisArticle.getId().toString()
                    ,thisArticle.getViewCount().intValue());
            redisCache.setCacheMap(LIU_LAN,map);
        }
        //根据文章id，在redis中更新浏览量
        redisCache.incrementCacheMapValue(LIU_LAN,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto article) {
        //将数据封装为Article类型，存入数据库
        Article copyBean = BeanCopyUtils.copyBean(article, Article.class);
        save(copyBean);

        //将文章对应的标签取出存入数据库（实现一对多的关系映射）
        List<ArticleTag> articleTags = article.getTags().stream().map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

}
