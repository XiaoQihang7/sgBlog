package com.sg.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    private Long viewCount;
    private Long categoryId;
    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;
    private String categoryName;
    private Date createTime;

}
