package com.sg;

import com.sg.domain.entity.User;
import com.sg.mapper.UserMapper;
import com.sg.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 每个测试方法后重新加载 Spring 上下文
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private List<User> backupData;

    @BeforeEach
    public void backupData() {
        // 备份数据
        backupData = userMapper.getAllUsers(); // 假设有一个方法可以获取所有用户数据
    }

    @AfterEach
    public void restoreData() {
        // 恢复数据
        userMapper.deleteAll();//这里删除数据会被回滚
        for (User user : backupData) {
            userMapper.insert(user); // 恢复备份的数据
        }
    }

    @Test
    @Transactional // 手动管理事务
    public void testInsertAsync() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            CompletableFuture<Void> future = userService.insertUserAsync(finalI);
            futures.add(future);
        }

        //调用 .get() 方法阻塞当前线程，直到所有异步操作都完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

        // 这里可以进行断言或其他操作

        long endTime = System.currentTimeMillis();
        System.out.println("共耗时：" + (endTime - startTime) + "ms");
    }
}