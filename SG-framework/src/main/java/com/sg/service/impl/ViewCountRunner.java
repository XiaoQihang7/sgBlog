package com.sg.service.impl;

import com.sg.domain.entity.Article;
import com.sg.service.ArticleService;
import com.sg.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 在项目启动时将数据库中的博客浏览量存入redis
     * @param args 系统参数
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //为确保浏览量准确性，先查看redis中是否存在数据
        Map<String, Object> cacheMap = redisCache.getCacheMap("article:viewCount");
        if (!(cacheMap.size()==0)){
            return;
        }

        List<Article> articleList = articleService.list();
        //将查出得到的数据类型转化，将需要的long类型数据转为合适类型的数据存入redis
        //这里只需要两个数据id和viewCount
        Map<String, Integer> viewCountMap = articleList.stream().collect(Collectors.toMap(
                        article -> article.getId().toString(), article -> article.getViewCount().intValue()
                )
        );
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
