package com.sg.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/23 21:17
 */
@Data
public class RoleReqDto {

    //角色id，更新角色信息时用到
    private Long id;

    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;

    //新增角色关联的菜单id
    private List<String> menuIds;
}
