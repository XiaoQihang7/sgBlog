package com.sg.controller;

import com.sg.annotation.SystemLog;
import com.sg.config.SwaggerConfig;
import com.sg.domain.ResponseResult;
import com.sg.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = SwaggerConfig.TAG_5)
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(BusinessName = "获取友链信息列表")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}