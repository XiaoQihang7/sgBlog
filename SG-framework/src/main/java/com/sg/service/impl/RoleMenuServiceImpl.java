package com.sg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.entity.RoleMenu;
import com.sg.service.RoleMenuService;
import com.sg.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 86156
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2024-04-23 21:30:34
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




