package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.RoleReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.RoleMenu;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.PageVo;
import com.sg.domain.vo.RoleVo;
import com.sg.mapper.RoleMapper;
import com.sg.mapper.RoleMenuMapper;
import com.sg.service.RoleService;
import com.sg.util.BeanCopyUtils;
import com.sg.util.SecurityUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:47
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    private static final int BATCH_SIZE = 50; // 可配置的批处理大小

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否为管理员 如果是返回集合中只需要有admin
        if(SecurityUtils.isAdmin()){
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //不是管理员则查询用户具有的角色信息，连表查询手写sql
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        // 参数校验
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        int start = (pageNum - 1) * pageSize;

        // 查询角色列表
        List<RoleVo> roles = roleMapper.selectRoleList(start, pageSize, roleName, status);

        //傻子返回这个页大小
//        long size = (long)roles.size();

        // 查询总记录数
        int total = roleMapper.selectRoleCount(roleName, status);
        // 计算总页数
        long totalPages = (long) Math.ceil((double) total / pageSize);

        PageVo pageVo = new PageVo(roles, totalPages);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRoleStatus(String roleId, String status) {
        // 参数校验
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(status)) {
            return ResponseResult.errorResult(508,"角色ID和状态不能为空");
        }
        if (!status.equals("0") && !status.equals("1")) {
            return ResponseResult.errorResult(508,"状态值只能为0或1");
        }

        // 更新角色状态
        int rowsAffected = roleMapper.updateRoleStatus(roleId, status);
        if (rowsAffected > 0) {
            return ResponseResult.okResult("角色状态更新成功");
        } else {
            return ResponseResult.errorResult(508,"角色状态更新失败");
        }
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleReqDto roleReqDto) {
        //todo 参数校验

        //1、将角色信息插入数据库，需要返回插入后的id
        Role role = BeanCopyUtils.copyBean(roleReqDto, Role.class);
        Long updateCount = roleMapper.addRoleReturnId(role);

        //2、将角色对应的菜单信息批量插入菜单表
        List<String> menuIds = roleReqDto.getMenuIds();
        //肯定不能使用map封装，key唯一
//        Map<Long, String> map = menuIds.stream().collect(Collectors.toMap(m -> roleId, m -> m));
        insertRoleMenus(role, menuIds);

        return ResponseResult.okResult();
    }

    //抽取一个公用的方法
    public void insertRoleMenus(Role role, List<String> menuIds) {
        List<RoleMenu> menus = menuIds.stream().map(m -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(Long.valueOf(m));
            return roleMenu;
        }).collect(Collectors.toList());
        //使用mybatis的批处理操作进行
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            RoleMenuMapper mapper = sqlSession.getMapper(RoleMenuMapper.class);
            for (int i = 0; i < menuIds.size() ; i++) {
                mapper.addRoleMenu(menus.get(i));
                if ((i + 1) % BATCH_SIZE == 0 || i == menuIds.size() - 1) {
                    sqlSession.flushStatements();
                }
            }
            } catch (Exception e) {
                // 可以在这里记录具体哪条数据导致了异常，并处理异常
                log.error("Error inserting user demo data: {}",e);
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR); // 重新抛出异常来执行全局回滚
            }
    }

    @Override
    public int deleteRoleById(Long id) {
        if (id == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }
        //简单单表操作使用mp封装好的
        return roleMapper.deleteById(id);
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, "0");
        List<Role> roles = roleMapper.selectList(wrapper);
        return ResponseResult.okResult(roles);
    }

    @Override
    public int updateRoleInfo(Role roleReqDto) {
        if (roleReqDto ==null || roleReqDto.getId() ==null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }
        return roleMapper.updateRoleInfo(roleReqDto);
    }
}

