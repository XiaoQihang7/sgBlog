package com.sg.domain.constants;

public class SystemConstants {
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /*
    * 数据库中存放的状态为char类型的数字，字符串类型方便操作
    * */
    public static final String STATUS_NORMAL = "0";

    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 浏览量前缀
     */
    public static final String LIU_LAN="article:viewCount";
}
