package com.sg.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/22 23:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    //角色ID@TableId
    private Long id;

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;

}
