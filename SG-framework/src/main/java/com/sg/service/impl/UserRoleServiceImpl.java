package com.sg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.domain.entity.UserRole;
import com.sg.service.UserRoleService;
import com.sg.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86156
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2024-04-27 11:57:34
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




