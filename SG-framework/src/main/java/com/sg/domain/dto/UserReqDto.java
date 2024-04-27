package com.sg.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/27 11:09
 */
@Data
public class UserReqDto {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;


    /**
     * 用户类型：0代表普通用户，1代表管理员
     */
//    private String type;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;


//    private String avatar;

    /**
     * 关联的角色id
     */
    private List<Long> roleIds;
}
