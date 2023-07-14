package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Tag;
import com.sg.domain.vo.PageVo;
import com.sg.domain.vo.TagListDto;
import com.sg.service.TagService;
import com.sg.mapper.TagMapper;
import com.sg.util.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86156
* @description 针对表【sg_tag(标签)】的数据库操作Service实现
* @createDate 2023-07-14 10:43:37
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{

    @Override
    public PageVo list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        if (null==tagListDto){
            List<Tag> list = list();
            return new PageVo(list,(long)list.size());
        }
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
//        queryWrapper.eq(!StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> dtoPage = new Page<>(pageNum, pageSize);
        List<Tag> records = page(dtoPage, queryWrapper).getRecords();
        return new PageVo(records,dtoPage.getTotal());
    }

    @Override
    public List<TagListDto> listAllTag() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(tagLambdaQueryWrapper);
        List<TagListDto> tagVos = BeanCopyUtils.copyBeanList(list, TagListDto.class);
        return tagVos;
    }

}




