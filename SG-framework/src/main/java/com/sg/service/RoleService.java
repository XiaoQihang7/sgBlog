package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:45
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

