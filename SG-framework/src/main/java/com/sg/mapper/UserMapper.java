package com.sg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-30 00:37:36
* @Entity com.sg.domain.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
