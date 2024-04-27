package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.LinkTestDate;
import com.sg.domain.entity.Link;

/**
* @author 86156
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2023-03-28 19:26:37
*/
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult selectDateTest(LinkTestDate date);
}

