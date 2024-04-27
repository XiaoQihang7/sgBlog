package com.sg;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.RoleReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.RoleMenu;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.mapper.RoleMapper;
import com.sg.mapper.RoleMenuMapper;
import com.sg.util.BeanCopyUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/23 22:10
 */
@SpringBootTest
public class roleMenuTest {

    private final  static Logger log = LoggerFactory.getLogger(roleMenuTest.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    private static final int BATCH_SIZE = 50; // 可配置的批处理大小

    @Test
    @Transactional
    public void testRoleMenu(){
        RoleReqDto roleReqDto = new RoleReqDto();
        roleReqDto.setRoleName("aaa");
        roleReqDto.setRoleKey("aaa");
        roleReqDto.setRoleSort(111);
        roleReqDto.setStatus("0");
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        roleReqDto.setMenuIds(list);
        //1、将角色信息插入数据库，需要返回插入后的id
        Role role = BeanCopyUtils.copyBean(roleReqDto, Role.class);
        //返回的是变更的行数
        Long updateCount = roleMapper.addRoleReturnId(role);

        //2、将角色对应的菜单信息批量插入菜单表
        List<String> menuIds = roleReqDto.getMenuIds();
        //肯定不能使用map封装，key唯一
//        Map<Long, String> map = menuIds.stream().collect(Collectors.toMap(m -> roleId, m -> m));
        List<RoleMenu> menus = menuIds.stream().map(m -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(Long.valueOf(m));
            return roleMenu;
        }).collect(Collectors.toList());
        //使用mybatis的批处理操作进行
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            RoleMenuMapper mapper = sqlSession.getMapper(RoleMenuMapper.class);
            for (int i = 0 ; i < menuIds.size() ; i++) {
                mapper.addRoleMenu(menus.get(i));
                if ((i + 1) % BATCH_SIZE == 0 || i == menuIds.size() - 1) {
                    sqlSession.flushStatements();
                }
            }
        } catch (Exception e) {
            // 可以在这里记录具体哪条数据导致了异常，并处理异常

            System.out.println(("Error inserting user demo data: " + e.getMessage()));
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR); // 重新抛出异常来执行全局回滚
        }finally {
            sqlSession.close();
        }

    }
}
