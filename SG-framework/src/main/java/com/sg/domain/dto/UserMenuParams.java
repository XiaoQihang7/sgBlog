package com.sg.domain.dto;

import lombok.Data;

@Data
public class UserMenuParams {
    private Integer userId;
    private Integer parentId;

    // 省略构造函数、getter 和 setter 方法
}
