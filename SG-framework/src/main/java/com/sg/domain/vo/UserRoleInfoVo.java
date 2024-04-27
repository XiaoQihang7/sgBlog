package com.sg.domain.vo;

import com.sg.domain.entity.Role;
import com.sg.domain.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/27 11:50
 */
@Data
public class UserRoleInfoVo {

    /**
     * 用户所关联的角色id列表
     */
    private List<Long> roleIds;

    /**
     * 所有角色的列表
     */
    private List<Role> roles;

    /**
     * 用户信息
     * 响应的json对象，对象名为user属性名（与类型无关）
     */
    private UserVo user;
}
