package com.parkinglot_backend;

import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.mapper.ShopLocationMapper;
import com.parkinglot_backend.util.RedisIdWorker;
import jakarta.annotation.Resource;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class ParkinglotBackendApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;

//    @Test
//    void testString(){
//        redisTemplate.opsForValue().set("name1","1112");
//        Object name = redisTemplate.opsForValue().get("name");
//        System.out.println(name);
//    }

    @Resource
    private RedisIdWorker redisIdWorker;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () ->{
            for (int i = 0; i < 100 ; i++){
                long id = redisIdWorker.nextId("order");
                System.out.println("id" + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0 ; i < 300 ; i++){
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time="+(end-begin));
    }



}
