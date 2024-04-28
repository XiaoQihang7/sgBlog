package com.sg.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/27 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenusVo {

    /**
     * 菜单树
     */
    private List<MenuVo> menus;

    /**
     * 角色所拥有的菜单权限id列表
     */
    private List<Long> checkedKeys;
}
