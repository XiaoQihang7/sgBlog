package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Article;
import com.sg.domain.vo.AddArticleDto;

import java.util.List;

public interface ArticleService extends IService<Article> {

    ResponseResult<List<Article>> hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);
}
