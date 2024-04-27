package com.sg.mapper;

import com.sg.domain.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Mapper
* @createDate 2024-04-23 21:30:34
* @Entity com.sg.domain.entity.RoleMenu
*/
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    int addRoleMenu(RoleMenu roleMenu);

    int deleteMenuByRoleId(Long id);
}




