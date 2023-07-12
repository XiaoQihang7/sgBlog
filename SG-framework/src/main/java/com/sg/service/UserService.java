package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.User;

/**
* @author 86156
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-03-30 00:37:36
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

}
