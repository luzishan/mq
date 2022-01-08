package com.snym.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 秒杀service
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/25 1:24
 */
@Service
@Slf4j
public class SecondKillService {

    @Autowired
    private RedisTemplate redisTemplate;

    public String doSecondKill(String uid, String prodid) {
        //uid和prodid非空判断
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(prodid)) {
            return "uid/prodid不能为空！";
        }
        //拼接key，库存key和用户key，一个用户只能秒杀一次，不可重复秒杀
        String kcKey = "sk:" + prodid + ":qt";
        String userKey = "sk:" + prodid + ":user";
        //监视库存
        redisTemplate.watch(kcKey);
        //获取库存
        Integer kc = (Integer) redisTemplate.opsForValue().get(kcKey);
        if (kc == null) {
            log.info("秒杀活动还未开始");
            return "秒杀活动还未开始";
        } else if (kc < 1) {
            log.info("秒杀结束");
            return "秒杀结束！";
        }
        //判断同一个用户是否重复秒杀,判断uid是否在集合userKey中
        Boolean b = redisTemplate.opsForSet().isMember(userKey, uid);
        if (b) {
            log.info("用户：" + uid + "已经秒杀过不可重复秒杀");
            return "用户：" + uid + "已经秒杀过不可重复秒杀";
        }
        //秒杀过程，减库存，将秒杀成功的用户添加到结合userKey中
        //开启事物
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        //组队
        redisTemplate.opsForValue().decrement(kcKey);
        redisTemplate.opsForSet().add(userKey, uid);
        //执行
        List exec = redisTemplate.exec();
        if (exec == null || exec.size() == 0) {
            log.info("秒杀失败");
            return "秒杀失败！";
        }
        log.info("秒杀成功");
        return "秒杀成功";
    }
}
