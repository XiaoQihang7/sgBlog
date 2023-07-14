package com.sg.service;

import com.sg.domain.ResponseResult;
import com.sg.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}