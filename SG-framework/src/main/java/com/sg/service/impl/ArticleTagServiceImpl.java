package com.sg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.entity.ArticleTag;
import com.sg.mapper.ArticleTagDao;
import com.sg.mapper.TagMapper;
import com.sg.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-26 21:09:25
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagDao, ArticleTag> implements ArticleTagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public boolean deleteTagsBatchByArticleIds(List<Long> as) {
        return tagMapper.deleteTagsBatchByArticleIds(as);
    }
}

