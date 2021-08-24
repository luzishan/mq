package com.snym.test;

import redis.clients.jedis.Jedis;

/**
 * Jedis测试
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/23 23:38
 */
public class JedisTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.0.106", 6379);
        String pong = jedis.ping();
        System.out.println(pong);
        jedis.close();
    }
}
