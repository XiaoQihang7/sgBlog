package com.sg.controller;

import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.dto.UserReqDto;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.service.UserService;
import com.sg.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/27 1:43
 */
@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 条件分页查询
     * @param pageNum 页偏移量
     * @param pageSize 页大小
     * @param userName 用户名
     * @param phonenumber 手机号
     * @param status 状态
     * @return 返回列表
     */
    @GetMapping("list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize,
                                      @RequestParam(required = false) String userName,
                                      @RequestParam(required = false) String phonenumber,
                                      @RequestParam(required = false) String status){
        return userService.getUserList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserReqDto userReqDto){
        return userService.addUser(userReqDto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    //todo 好奇，写了序列化转换器之后，接收id类型可以使用String来接收吗？
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        //不能删除当前操作的用户
        if (SecurityUtils.getUserId().equals(id)){
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        boolean b = userService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getUserInfoById(@PathVariable("id") Long id){
        return userService.getUserInfoById(id);
    }

    @PutMapping
    public ResponseResult updateUserRoleInfo(@RequestBody UserReqDto userReqDto){
        return userService.updateUserRoleInfo(userReqDto);
    }

}
