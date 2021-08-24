package com.snym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis集成springbootc测试
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/23 23:51
 */
@RestController
@RequestMapping("/redisTest")
public class RedisTestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/redis")
    public String testRedis(){
        redisTemplate.opsForValue().set("name","haha");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println((String)name);
        return (String)name;
    }
}
