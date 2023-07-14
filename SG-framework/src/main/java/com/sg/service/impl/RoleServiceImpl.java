package com.sg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.entity.Role;
import com.sg.mapper.RoleMapper;
import com.sg.service.RoleService;
import com.sg.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:47
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否为管理员 如果是返回集合中只需要有admin
        if(SecurityUtils.isAdmin()){
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //不是管理员则查询用户具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}

