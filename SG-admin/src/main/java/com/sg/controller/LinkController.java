package com.sg.controller;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Link;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/27 14:14
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult linkPageList(Integer pageNum , Integer pageSize ,
                                       String name , String status){
        return linkService.linkPageList(pageNum,pageSize,name,status);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkByid(@PathVariable("id") Long id){
        if(id==null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }
        Link byId = linkService.getById(id);
        return ResponseResult.okResult(byId);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @PutMapping()
    public ResponseResult updateLink(@RequestBody Link link){
        if(link==null || link.getId()==null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        if(id==null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }
        linkService.removeById(id);
        return ResponseResult.okResult();
    }


}
