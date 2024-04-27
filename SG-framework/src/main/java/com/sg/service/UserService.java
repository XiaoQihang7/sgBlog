package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.UserReqDto;
import com.sg.domain.entity.User;
import io.netty.util.concurrent.CompleteFuture;

import java.util.concurrent.CompletableFuture;

/**
* @author 86156
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-03-30 00:37:36
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    CompletableFuture<Void> insertUserAsync(int i);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserReqDto userReqDto);

    ResponseResult getUserInfoById(Long id);

    ResponseResult updateUserRoleInfo(UserReqDto userReqDto);

    boolean deleteById(Long id);
}
