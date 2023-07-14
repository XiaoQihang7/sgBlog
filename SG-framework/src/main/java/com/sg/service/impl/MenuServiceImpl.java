package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Menu;
import com.sg.mapper.MenuMapper;
import com.sg.util.SecurityUtils;
import org.springframework.stereotype.Service;
import com.sg.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author QiHang777
 * @since 2023-07-13 10:49:50
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员id=1，返回所有权限
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
            menuQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            menuQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(menuQueryWrapper);
            return list.stream().map(Menu::getPerms).collect(Collectors.toList());
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                //获取到最上层父类路由存为menu
                .filter(menu -> menu.getParentId().equals(parentId))
                //获取直属于父类路由的子路由
                .map(menu -> {
                    menu.setChildren(getChildren(menu,menus));
                    return menu;
                })
                .collect(Collectors.toList());
        return menuTree;
    }


    /**
     * 获取存入参数的 子Menu集合
     * @param menu 传入的需要封装子路由的对象参数
     * @param menus 包含父路由和子路由的完整参数
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> {
                    m.setChildren(getChildren(m,menus));
                    return m;
        })
                .collect(Collectors.toList());
        return childrenList;
    }
}

