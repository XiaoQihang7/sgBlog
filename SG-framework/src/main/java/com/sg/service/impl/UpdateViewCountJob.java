package com.sg.service.impl;


import com.sg.domain.entity.Article;
import com.sg.service.ArticleService;
import com.sg.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sg.domain.constants.SystemConstants.LIU_LAN;


/**
 * 使用定时任务将redis中的数据同步到数据库中去
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "* 0/10 * * * ?") //从0分钟开始每十分钟执行一次
    //todo 检查这里是不是有问题，执行太多次相同的sql了
    public void UpdateViewCount(){
        //查询redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap(LIU_LAN);
        //将数据封装为list对象集合
        List<Article> articles = cacheMap.entrySet().stream().map(m -> new Article(Long.valueOf(m.getKey()),
                        m.getValue()))
                .collect(Collectors.toList());

        articleService.updateBatchById(articles);

    }
}
