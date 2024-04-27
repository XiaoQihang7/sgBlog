package com.sg.controller;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.RoleReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.RoleMenu;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.PageVo;
import com.sg.domain.vo.RoleVo;
import com.sg.mapper.RoleMenuMapper;
import com.sg.service.MenuService;
import com.sg.service.RoleMenuService;
import com.sg.service.RoleService;
import com.sg.util.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/22 23:18
 */
@RestController
@RequestMapping("system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @GetMapping("/list")
    //todo 这请求参数没问题？ 不需要@RequestParam(required = false)? 还真可以请求到，不存在的值默认为空,那这个注解有什么用
    public ResponseResult roleList(@RequestParam("pageNum") Integer pn ,Integer pageSize ,
                                   String roleName , String status){
        return roleService.roleList(pn,pageSize,roleName,status);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        //查询所有状态正常的角色
        return roleService.listAllRole();
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Map<String, String> requestBody){
        String roleId = requestBody.get("roleId");
        String status = requestBody.get("status");
        return roleService.updateRoleStatus(roleId, status);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleReqDto roleReqDto){
        return roleService.addRole(roleReqDto);
    }


    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Long id){
        if (null==id){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR);
        }
        Role role = roleService.getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @PutMapping
    @Transactional
    public ResponseResult updateRoleInfo(@RequestBody RoleReqDto roleReqDto){
        if (roleReqDto == null || roleReqDto.getId()==null){
            throw  new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }

        //角色下绑定了多个菜单，先将菜单进行删除再插入
        //根据角色id删除其下菜单信息
        roleMenuMapper.deleteMenuByRoleId(roleReqDto.getId());

        //根据id修改角色信息
        //todo 这里转换为role会不会比直接传入原数据好一点？但是转换也是需要时间和代码量
        Role role = BeanCopyUtils.copyBean(roleReqDto, Role.class);
        //这个修改是修改填了值的，为填值不覆盖，还是为原来的值
        roleService.updateRoleInfo(role);

        //将角色对应的菜单权限批量插入
        List<String> menuIds = roleReqDto.getMenuIds();
        roleService.insertRoleMenus(role,menuIds);

        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        //根据id删除角色信息
        //todo 探究一下mp的删除是否需要根据主键删除
        int i = roleService.deleteRoleById(id);
        //删除角色信息关联的菜单信息
        int i1 = roleMenuMapper.deleteMenuByRoleId(id);

        if (i == 0 || i1 == 0){
            return ResponseResult.errorResult(518,"删除角色为空");
        }

        //如果用户绑定了角色，需要做校验？
        //应该不用，在用户查询权限时会根据角色查询权限，查不到为空
        return ResponseResult.okResult();
    }


}
