package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author QiHang777
 * @since 2023-03-27 23:05:38
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}

