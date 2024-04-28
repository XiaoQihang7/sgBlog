package com.sg.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户和角色关联表
 * @TableName sys_user_role
 */
@TableName(value ="sys_user_role")
@Data
public class UserRole implements Serializable {
    /**
     * 用户ID
     */
//    @TableId
    private Long userId;

    /**
     * 角色ID
     */
//    @TableId
    private Long roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}