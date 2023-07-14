package com.sg.controller;

import com.sg.domain.ResponseResult;
import com.sg.domain.entity.ArticleTag;
import com.sg.domain.entity.Tag;
import com.sg.domain.vo.PageVo;
import com.sg.domain.vo.TagListDto;
import com.sg.service.TagService;
import com.sg.util.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum , Integer pageSize , TagListDto tagListDto){
        PageVo list = tagService.list(pageNum , pageSize , tagListDto);
        return ResponseResult.okResult(list);
    }

    @PostMapping
    public ResponseResult addTag(Tag tagListDto){
        tagService.save(tagListDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @PutMapping()
    public ResponseResult updateTag(Tag tagListDto){
        tagService.updateById(tagListDto);
        return ResponseResult.okResult();
    }

    @GetMapping("listAllTag")
    public ResponseResult listAllTag(){
//        tagService.listAllTag();
        List<Tag> list = tagService.list();
        List<TagListDto> tagListDtos = BeanCopyUtils.copyBeanList(list, TagListDto.class);
        return ResponseResult.okResult(tagListDtos);
    }
}
