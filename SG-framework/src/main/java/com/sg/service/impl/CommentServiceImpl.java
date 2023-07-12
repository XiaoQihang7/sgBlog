package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Comment;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.CommentVo;
import com.sg.domain.vo.PageVo;
import com.sg.service.CommentService;
import com.sg.mapper.CommentMapper;
import com.sg.service.UserService;
import com.sg.util.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86156
* @description 针对表【sg_comment(评论表)】的数据库操作Service实现
* @createDate 2023-03-30 16:49:41
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{


    @Autowired
    UserService userService;

    /**
     *
     * 【 查询文章评论 ， 后与查询友链评论进行复用此同一个接口
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @param articleCommentType 文章评论类型
     * @return
     */
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize, String articleCommentType) {

        //1、根据根评论id和文章id ， 分页查询根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(articleCommentType),Comment::getArticleId,articleId);

        queryWrapper.eq(Comment::getRootId,-1)
                .eq(Comment::getType,articleCommentType);

        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        Page<Comment> page = page(commentPage,queryWrapper);

        List<Comment> pageRecords = page.getRecords();
        //2、将page数据转换成前端需要的数据
        //考虑到根评论和子评论类似，转换数据格式和转换操作相同，故将此方法封装为一个通用方法
        List<CommentVo> commentVoList = toCommentVoList(pageRecords);

        //3、查询子评论，并将其赋值给根评论
        //如何找子评论？- 子评论的格式中，rootId指向的是评论id(主键)
        /**
         * 注意这里，只能是根据id找对应的rootId；如果顺序颠倒，遍历时会使 根评论和子评论颠倒
         * 其实就是根据根评论收集子评论
         */
        commentVoList.stream().map(c->c.setChildren(getChildren(c.getId())))
                .collect(Collectors.toList());


        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    /**
     * 【 查询友链评论
     * @param pageNum
     * @param pageSize
     * @param articleId
     * @param commentType
     * @return
     */
    @Override
    public ResponseResult getLinkComments(Integer pageNum, Integer pageSize, Long articleId, String commentType) {
//查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);

        //评论类型
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getRootId,id);
        wrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentCList = list(wrapper);
        return toCommentVoList(commentCList);
    }

    //将查到的评论信息转换为Vo，Vo肯定是需要用户名称的
    //todo 这里的业务功能是一次查询出所有的评论，要是评论过多，可以优化一下，一次查询多少条子评论
    private List<CommentVo> toCommentVoList(List<Comment> records) {
        //首先，将需要的字段拷贝进入Vo映射
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        //遍历vo，并赋予昵称（给没有的字段赋值）
        //不止于此，还要给查到的子评论赋予昵称；
        commentVos.stream().map(
                c->{c.setUsername(userService.getById(c.getCreateBy()).getNickName());
                    //方便查询子评论复用
                    if(c.getToCommentUserId()!=-1){
                        String toCommentUserName = userService.getById(c.getToCommentUserId()).getNickName();
                        c.setToCommentUserName(toCommentUserName);
                    }
                return c;
                }
        ).collect(Collectors.toList());
        return commentVos;
    }


    /**
     *
     * @param comment 添加评论
     * @return 返回给前端指定格式
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //进行后端校验，评论不可为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }
}




