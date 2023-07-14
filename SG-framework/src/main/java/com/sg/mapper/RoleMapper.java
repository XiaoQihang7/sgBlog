package com.sg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:42
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

