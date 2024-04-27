package com.sg.mapper;

import com.sg.domain.ResponseResult;
import com.sg.domain.dto.LinkTestDate;
import com.sg.domain.entity.Link;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 86156
* @description 针对表【sg_link(友链)】的数据库操作Mapper
* @createDate 2023-03-28 19:26:37
* @Entity com.sg.domain/entity.Link
*/
@Mapper
public interface LinkMapper extends BaseMapper<Link> {


    List<LinkTestDate> selectDateTest(LinkTestDate date);
}
