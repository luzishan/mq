package com.snym.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 秒杀案例
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/25 0:33
 */
@RestController
@RequestMapping("/secKill")
@Slf4j
public class SecondKillController {

    @Autowired
    private RedisTemplate redisTemplate;

    static String secKillScript = "local userid=KEYS[1];\r\n" +
            "local prodid=KEYS[2];\r\n" +
            "local qtkey='sk:'..prodid..\":qt\";\r\n" +
            "local usersKey='sk:'..prodid..\":user\";\r\n" +
            "local userExists=redis.call(\"sismember\",usersKey,userid);\r\n" +
            "if tonumber(userExists)==1 then \r\n" +
            "   return 2;\r\n" +
            "end\r\n" +
            "local num= redis.call(\"get\" ,qtkey);\r\n" +
            "if tonumber(num)<=0 then \r\n" +
            "   return 0;\r\n" +
            "else \r\n" +
            "   redis.call(\"decr\",qtkey);\r\n" +
            "   redis.call(\"sadd\",usersKey,userid);\r\n" +
            "end\r\n" +
            "return 1";


    @GetMapping("doKill/{prodid}")
    public String doSecondKill(@PathVariable("prodid") String prodid) {
        String uid = new Random().nextInt(50000) + "";
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

    @GetMapping("doKill1/{prodid}")
    public String doSecondKill1(@PathVariable("prodid") String prodid) {
        String uid = UUID.randomUUID().toString();
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(String.class);
        defaultRedisScript.setScriptText(secKillScript);
        List<String> keyList = new ArrayList<>();
        String kcKey = "sk:" + prodid + ":qt";
        String userKey = "sk:" + prodid + ":user";
        keyList.add(kcKey);
        keyList.add(userKey);
        Object execute = redisTemplate.execute(defaultRedisScript, keyList, uid);
        String reString = String.valueOf(execute);
        if ("0".equals(reString)) {
            log.info("已抢空！！");
        } else if ("1".equals(reString)) {
            log.info("抢购成功！！！！");
        } else if ("2".equals(reString)) {
            log.info("该用户已抢过！！");
        } else {
            log.info("抢购异常！！");
        }
        return "";
    }

    @GetMapping("init")
    public String initKc(Integer kc, String prodid) {
        String kcKey = "sk:" + prodid + ":qt";
        redisTemplate.opsForValue().set(kcKey, kc);
        return "初始化库存成功";
    }
}
