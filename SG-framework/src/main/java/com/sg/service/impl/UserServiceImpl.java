package com.sg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.UserReqDto;
import com.sg.domain.entity.Role;
import com.sg.domain.entity.User;
import com.sg.domain.entity.UserRole;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.*;
import com.sg.mapper.RoleMapper;
import com.sg.mapper.UserRoleMapper;
import com.sg.service.UserService;
import com.sg.mapper.UserMapper;
import com.sg.util.BeanCopyUtils;
import com.sg.util.SecurityUtils;
import io.netty.util.concurrent.CompleteFuture;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 86156
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-03-30 00:37:36
*/
@Service
@EnableTransactionManagement
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName) {
        Integer count = query().eq("nick_name", nickName).count();
        return count >= 1; //存在返回true
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(userName),User::getUserName,userName);
        int count = count(queryWrapper);
        return count >= 1;
    }

    /**
     * 用于多线程的测试
     * @param i 伪装数据
     * @return 返回异步结果
     */
    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> insertUserAsync(int i) {
        User user = new User();
        user.setUserName("祁行" + i);
        user.setNickName("优秀的家伙" + i);
        user.setPassword("1234");
        int i1 = userMapper.insertOne(user);

        Thread thread = Thread.currentThread();

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize,
                                      String userName, String phonenumber, String status) {
        //参数校验
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;
        pageNum = (pageNum - 1) * pageSize;

        //todo 探究mp的分页查询是否也是封装成这样的
        List<UserVo> userList = userMapper.getUserList(pageNum, pageSize, userName, phonenumber, status);
        int count = userMapper.getUserListCount(userName,phonenumber,status);
        long sumCount = (long) Math.ceil((double) count/pageSize);
        PageVo pageVo = new PageVo(userList,sumCount);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUser(UserReqDto userReqDto) {
        //需要新增用户功能。新增用户时可以直接关联角色。
        //新增用户时注意密码加密存储。
        //用户名不能为空，否则提示：必需填写用户名
        //用户名必须之前未存在，否则提示：用户名已存在
        //手机号必须之前未存在，否则提示：手机号已存在
        //邮箱必须之前未存在，否则提示：邮箱已存在
        //直接复用客户端的注册用户
        User user = BeanCopyUtils.copyBean(userReqDto, User.class);
        register(user);

        List<Long> roleIds = userReqDto.getRoleIds();
        Map<Long, Long> map = roleIds.stream().collect(Collectors.toMap(u -> user.getId(), u -> u));
        //批量插入用户角色信息
        userMapper.addUserRole(map);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfoById(Long id) {
        //查询用户对应的角色id
        List<Object> objects = userRoleMapper.selectObjs(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId,id)
                .select(UserRole::getRoleId));
        List<Long> roleIds = objects.stream().map(o -> {
            return (Long) o;
        }).collect(Collectors.toList());
        UserRoleInfoVo userRoleInfoVo = new UserRoleInfoVo();
        userRoleInfoVo.setRoleIds(roleIds);

        //查询所有角色列表
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getStatus , "0");
        roleWrapper.eq(Role::getDelFlag , "0");
        List<Role> roles = roleMapper.selectList(roleWrapper);
        userRoleInfoVo.setRoles(roles);

        //查询用户信息
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getId , id);
        User user = userMapper.selectById(id);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
//        User copyBean = BeanCopyUtils.copyBean(userVo, User.class);
        userRoleInfoVo.setUser(userVo);
        return ResponseResult.okResult(userRoleInfoVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUserRoleInfo(UserReqDto userReqDto) {

        if (userReqDto == null || userReqDto.getId() == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_ERROR);
        }

        //todo 这个Type里面其它的类型是什么
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserRoleMapper mapper = sqlSession.getMapper(UserRoleMapper.class);

        //1、更新对象信息
        User user = BeanCopyUtils.copyBean(userReqDto, User.class);
        updateById(user);
        //2、删除用户原有的角色信息
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId,user.getId());
        mapper.delete(updateWrapper);
        //3、插入角色信息
        List<Long> roleIds = userReqDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream().map(r -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(r);
            return userRole;
        }).collect(Collectors.toList());
        for (int i = 0 ; i < userRoles.size() ; i++){
            mapper.insert(userRoles.get(i));
            if ((i+1)%50 == 0 || i == userRoles.size()-1){
                sqlSession.flushStatements();
            }
        }
        return ResponseResult.okResult();
    }

    @Override
    public boolean deleteById(Long id) {
        return userMapper.delById(id);
    }
}
