package com.sg.mapper;

import com.sg.domain.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Mapper
* @createDate 2024-04-27 11:57:34
* @Entity com.sg.domain.entity.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}




