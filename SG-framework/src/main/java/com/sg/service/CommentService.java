package com.sg.service;

import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86156
* @description 针对表【sg_comment(评论表)】的数据库操作Service
* @createDate 2023-03-30 16:49:41
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize, String articleComment);

    ResponseResult addComment(Comment comment);

    ResponseResult getLinkComments(Integer pageNum, Integer pageSize, Long articleId, String commentTyp);
}
