package com.sg.controller;

import com.sg.domain.ResponseResult;
import com.sg.domain.dto.ArticleDto;
import com.sg.domain.vo.AddArticleDto;
import com.sg.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "文章控制器")
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult getListArticle(@RequestParam("pageNum") Integer pn , @RequestParam("pageSize") Integer ps ,String title , String summary){
        return articleService.listArticle(pn,ps,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable("id") Long id){
        return articleService.getArticle(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody  ArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseResult delectArticel(@PathVariable("id") long id){
        boolean b = articleService.removeById(id);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(408,"删除文章失败");
    }


}