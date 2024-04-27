package com.sg.mapper;

import com.sg.domain.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86156
* @description 针对表【sg_tag(标签)】的数据库操作Mapper
* @createDate 2023-07-14 10:43:37
* @Entity com.sg.domain.entity.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    boolean deleteTagsBatchByArticleIds(@Param("as") List<Long> as);
}




