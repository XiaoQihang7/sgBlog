package com.sg.service.impl;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.User;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.BlogUserLoginVo;
import com.sg.domain.vo.LoginUser;
import com.sg.domain.vo.UserInfoVo;
import com.sg.service.BlogLoginService;
import com.sg.util.BeanCopyUtils;
import com.sg.util.JwtUtil;
import com.sg.util.RedisCache;
import com.sg.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //封装前端传来的登入表单，作为authenticate的参数
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //校验，需要重写UserDetailsService
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //生成token
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
//        redisCache.setCacheObject("bloglogin:"+userId,loginUser,30, TimeUnit.MINUTES);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }

    /**
     * 需要查看一下这里的前端校验情况，什么时候会出现登入已过期的弹出窗口
     * --前端得到数据状态为401时，需要重新登入
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //java.lang.String cannot be cast to com.sg.domain.vo.LoginUser
        //如果出现以上错误，说明你的redis中并没有存入用户信息，这是因为浏览器保存了token所以再次使用时无需登入，而redis中值已过期
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
//            return ResponseResult.okResult("登入已过期");
//        }
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) authentication.getPrincipal();
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}