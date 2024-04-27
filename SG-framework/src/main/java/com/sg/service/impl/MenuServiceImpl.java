package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.ResponseResult;
import com.sg.domain.constants.SystemConstants;
import com.sg.domain.entity.Menu;
import com.sg.domain.vo.MenuVo;
import com.sg.mapper.MenuMapper;
import com.sg.util.BuildTreeUtils;
import com.sg.util.SecurityUtils;
import org.springframework.stereotype.Service;
import com.sg.service.MenuService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        //单表查询菜单表，使用mp
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
            menuQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            menuQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(menuQueryWrapper);
            return list.stream().map(Menu::getPerms).collect(Collectors.toList());
        }
        //rbac连表查询，手写sql
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
//        List<Menu> menuTree = builderMenuTree(menus,0L);
        List<Menu> menuTree = getUserMenuTree(menus);
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
     * @return 返回封装好的树结构
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))//当前层级没有子菜单，递归结束
                .map(m -> {
                    m.setChildren(getChildren(m,menus));
                    return m;
        })
                .collect(Collectors.toList());
        return childrenList;
    }

    /**
     * O(n)
     * @param menus 菜单数
     * @return 返回树结构
     */
    public List<Menu> getUserMenuTree(List<Menu> menus){
        //1、将得到的所有路由信息存入map中便于存、取
        Map<Long, Menu> menuMap = menus.stream().collect(Collectors.toMap(Menu::getId, menu -> menu));

        //2、将顶层父结构存入menuTree
        //优化一下，这段代码可放入树结构构建的循环中
//        List<Menu> menuTree = menus.stream().filter(m -> m.getParentId().equals(0L)).collect(Collectors.toList());
        ArrayList<Menu> menuTree = new ArrayList<>();

        //3、一个循环构建树结构
        for (Menu menu : menus){
            Long parentId = menu.getParentId();
            if (parentId.equals(0L)) {
                // 如果是顶层菜单，则直接添加到树中
                menuTree.add(menu);
            }else {
                Menu parentMenu = menuMap.get(parentId);
                if (null != parentMenu) {
                    if (null == parentMenu.getChildren()) {
                        parentMenu.setChildren(new ArrayList<>());
                    }
                    parentMenu.getChildren().add(menu);
                }
            }
        }
        return menuTree;
    }

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        wrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
        List<Menu> menuList = list(wrapper);
        return ResponseResult.okResult(menuList);
    }

    @Override
    public ResponseResult getMenu(long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //不可把父类菜单设置为当前菜单
        if (menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(500,"修改菜单"+ menu.getMenuName()+"失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(long menuId) {
        //如果要删除的菜单有子菜单则提示：存在子菜单不允许删除 并且删除失败。
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,menuId);
        int count = count(wrapper);
        if (count > 0){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除,删除失败");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeSelect() {
        List<MenuVo> rootMenuTree = this.getBaseMapper().getRootMenuTree();
        return ResponseResult.okResult(rootMenuTree);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        // 根据角色id查询对应的菜单树结构
        List<MenuVo> rootMenuTree = this.getBaseMapper().getRootMenuTreeById(id);
//        getUserMenuTree()
        BuildTreeUtils.buildTree(rootMenuTree);
        return ResponseResult.okResult(rootMenuTree);
    }
}

