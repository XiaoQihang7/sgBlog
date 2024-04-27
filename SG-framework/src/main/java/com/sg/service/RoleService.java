package com.sg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.RoleReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:45
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult roleList(Integer pn, Integer pageSize, String roleName, String status);

    ResponseResult updateRoleStatus(String roleId, String status);

    ResponseResult addRole(RoleReqDto roleReqDto);

    int updateRoleInfo(Role roleReqDto);

    public void insertRoleMenus(Role role, List<String> menuIds);

    int deleteRoleById(Long id);

    ResponseResult listAllRole();

}

