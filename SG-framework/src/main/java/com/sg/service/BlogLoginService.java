package com.sg.service;

import com.sg.domain.ResponseResult;
import com.sg.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
