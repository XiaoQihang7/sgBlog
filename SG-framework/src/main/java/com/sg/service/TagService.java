package com.sg.service;

import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.vo.PageVo;
import com.sg.domain.vo.TagListDto;

import java.util.List;

/**
* @author 86156
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2023-07-14 10:43:37
*/
public interface TagService extends IService<Tag> {

    PageVo list(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagListDto> listAllTag();
}
