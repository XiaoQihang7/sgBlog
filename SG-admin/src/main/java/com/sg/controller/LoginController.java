package com.sg.controller;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Menu;
import com.sg.domain.entity.User;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.AdminUserInfoVo;
import com.sg.domain.vo.LoginUser;
import com.sg.domain.vo.RoutersVo;
import com.sg.domain.vo.UserInfoVo;
import com.sg.service.LoginService;
import com.sg.service.MenuService;
import com.sg.service.RoleService;
import com.sg.util.BeanCopyUtils;
import com.sg.util.RedisCache;
import com.sg.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //查当前登入用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //根据用户id查询权限信息
        Long id = loginUser.getUser().getId();
        List<String> perms = menuService.selectPermsByUserId(id);

        //根据用户id查询角色信息
        List<String> roleKey = roleService.selectRoleKeyByUserId(id);

        //查询用户信息
        User user = loginUser.getUser();
        UserInfoVo userVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(new AdminUserInfoVo(perms,roleKey,userVo));
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu结果以tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}