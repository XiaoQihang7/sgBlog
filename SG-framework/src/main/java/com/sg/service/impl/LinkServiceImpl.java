package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.ResponseResult;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Link;
import com.sg.domain.vo.LinkVo;
import com.sg.service.LinkService;
import com.sg.mapper.LinkMapper;
import com.sg.util.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86156
* @description 针对表【sg_link(友链)】的数据库操作Service实现
* @createDate 2023-03-28 19:26:37
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService{

    @Override
    public ResponseResult getAllLink() {
        //查所有审核通过的链接
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.STATUS_NORMAL);
        List<Link> linkList = list(queryWrapper);
        //转换为vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

}
