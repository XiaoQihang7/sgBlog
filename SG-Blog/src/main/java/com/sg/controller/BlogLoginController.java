package com.sg.controller;

import com.sg.Exception.SystemException;
import com.sg.config.SwaggerConfig;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.User;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.service.BlogLoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//进行后端校验
@RestController
@Api(tags = SwaggerConfig.TAG_3)
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //Springsecurity登入校验是通过用户名去查数据库比对的
        
        if(!StringUtils.hasText(user.getUserName())){//用户名为空或不存在
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
