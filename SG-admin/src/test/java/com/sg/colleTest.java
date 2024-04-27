package com.sg;

import com.sg.domain.entity.Menu;
import com.sg.domain.vo.MenuVo;
import com.sg.mapper.MenuMapper;
import com.sg.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/18 23:40
 */
@SpringBootTest
public class colleTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void testCollections(){
/*//        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(3L);
        //这里直接传入5，会将这个5赋值给sql中的所有参数
        List<Menu> menuss = menuMapper.selectRouterPTreeByUserId(5L,0L);
//        System.out.println(menus);
        System.out.println(menuss);
//        System.out.println(menuss.equals(menus));*/
        List<MenuVo> rootMenuTree = menuMapper.getRootMenuTree();
        System.out.println(rootMenuTree);

    }

}
