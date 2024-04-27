package com.sg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.domain.entity.User;
import com.sg.domain.vo.UserInfoVo;
import com.sg.domain.vo.UserVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;
import java.util.Map;

/**
* @author 86156
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-30 00:37:36
* @Entity com.sg.domain.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Integer insertBatch(@Param("users") List<User> users);

    List<Integer> insertAndGetIds(List<User> users);

//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertOne(User user);

    List<User> getAllUsers();

    Integer deleteAll();

    List<User> getUserByCondition(User user);

    List<User> getUserByConditions(User user);

    List<UserVo> getUserList(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                             @Param("userName") String userName,
                             @Param("phonenumber") String phonenumber,
                             @Param("status") String status);

    int getUserListCount( @Param("userName") String userName,
                          @Param("phonenumber") String phonenumber,
                          @Param("status") String status);

    int addUserRole(Map<Long, Long> map);

    boolean delById(Long id);
}
