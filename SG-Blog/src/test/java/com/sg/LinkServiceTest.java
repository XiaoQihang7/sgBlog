package com.sg;

import com.sg.domain.ResponseResult;
import com.sg.domain.dto.LinkTestDate;
import com.sg.domain.entity.User;
import com.sg.mapper.UserMapper;
import com.sg.service.LinkService;
import com.sg.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/5 20:56
 */
@SpringBootTest
@Slf4j
public class LinkServiceTest {

    @Autowired
    private LinkService linkService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskExecutor taskExecutor;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testLinkDate(){
        LinkTestDate linkTestDate = new LinkTestDate();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        System.out.println(format);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Instant instant = date.toInstant();
//        System.out.println(instant); //2024-04-05T13:14:57.489Z
//        dateTimeFormatter.format(date.toInstant());//Unsupported field: YearOfEra
        System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
        linkTestDate.setCreateTime(new Date());
        ResponseResult responseResult = linkService.selectDateTest(linkTestDate);
        System.out.println(responseResult.getData().toString());

    }

    /**
     * 写一个批量插入的sql
     * 加事务比较
     * 使用多线程插入多个对象
     */
    @Test
    @Transactional
    public void insertBatch(){
//        User user = new User();
        long startTime = System.currentTimeMillis();
        ArrayList<User> userList = new ArrayList<>(100);//size==0
        User[] userArray = new User[100];
//        List<User> userList = Arrays.asList(userArray);//不能再添加元素
//        System.out.println(userList);
        AtomicInteger a = new AtomicInteger(1);
        Arrays.stream(userArray).forEach(user-> {
            user = new User();
//            System.out.println(a);
            user.setUserName("祁行" + a);
            user.setNickName("优秀的家伙" + a);
            user.setPassword("1234");
//            System.out.println(user);
            userList.add(user);
            a.getAndIncrement();//先返回当前值再自增1。
//            int i = a.addAndGet(1); //先增加指定的值再返回增加后的结果。
        });
        Integer integers = userMapper.insertBatch(userList);
        userList.stream().map(User::getId).forEach(System.out::println);
        log.info("插入成功{}",integers);
        log.info("共耗时{}",System.currentTimeMillis()-startTime);
    }

    /**
     * 难道sql嵌套真的是不能实现？！
     */
    @Test
    @Transactional
    public void insertAndGetIds(){
        User user = new User();
        user.setUserName("祁行");
        user.setNickName("优秀的家伙");
        user.setPassword("1234");
        User user1 = new User();
        user1.setUserName("祁行1");
        user1.setNickName("优秀的家伙1");
        user1.setPassword("1234");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        List<Integer> integers = userMapper.insertAndGetIds(users);
        log.info("插入成功{}",integers);
    }

    @Test
    public void testSelectCondition(){
        User user = new User();
        user.setId(1l);
        user.setUserName("sg");
        user.setNickName("sg333");
        List<User> userByConditions = userMapper.getUserByConditions(user);
        //只能使用一个条件进行查询
        user.setId(null);
        List<User> userByCondition = userMapper.getUserByCondition(user);
        log.info("单条件查询  ："+userByCondition.toString());
        log.info("多条件查询  ："+userByConditions.toString());
    }

}
