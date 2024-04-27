package com.sg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.domain.dto.RoleReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.RoleMenu;
import com.sg.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author QiHang777
 * @since 2023-07-13 11:01:42
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    List<RoleVo> selectRoleList(@Param("start") int start, @Param("pageSize") Integer pageSize,
                                @Param("roleName") String roleName, @Param("status") String status);


    int selectRoleCount( @Param("roleName") String roleName, @Param("status") String status);

    int updateRoleStatus(String roleId, String status);

    Long addRoleReturnId(Role role);

    int updateRoleInfo(Role roleReqDto);

}

