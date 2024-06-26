package com.sg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.domain.entity.Menu;
import com.sg.domain.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author QiHang777
 * @since 2023-07-13 10:47:01
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuVo> getRootMenuTree();

    List<MenuVo> getRootMenuTreeById(Long id);

    List<MenuVo> getAllMenuTreeNode();
}

