package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author QiHang777
 * @since 2023-07-13 10:49:22
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}

