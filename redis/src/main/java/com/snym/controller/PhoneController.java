package com.snym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 模拟手机验证码
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/24 0:03
 */
@RestController
@RequestMapping("/phone")
public class PhoneController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("verificationCode/{phoneNumber}/{phoneCode}")
    public String verificationCode(@PathVariable("phoneNumber") String phoneNumber,
                                   @PathVariable("phoneCode") String phoneCode) {
        String resultMsg = "验证通过";
        String countPhoneKey = "count" + phoneNumber;
        Object phoneKey = redisTemplate.opsForValue().get(countPhoneKey);
        if (phoneKey == null) {
            resultMsg = extracted(phoneNumber, phoneCode, countPhoneKey,resultMsg);
        } else {
            if ((Integer) (phoneKey) > 2) {
                resultMsg = "一天只能验证三次";
            } else {
                resultMsg = extracted(phoneNumber, phoneCode, countPhoneKey,resultMsg);
            }
        }

        return resultMsg;
    }

    private String extracted(String phoneNumber, String phoneCode, String countPhoneKey,String resultMsg) {
        String code = (String) redisTemplate.opsForValue().get("code" + phoneNumber);
        if (phoneCode.equals(code)) {
            redisTemplate.opsForValue().set(countPhoneKey, 0);
            resultMsg = "验证通过";
        } else {
            redisTemplate.opsForValue().increment(countPhoneKey);
            resultMsg = "验证失败";
        }
        return resultMsg;
    }

    //设置验证码
    @GetMapping("setCode/{phoneNumber}")
    public String setVerificationCode(@PathVariable("phoneNumber") String phoneNumber) {
        String verificationCode = getVerificationCode();
        redisTemplate.opsForValue().set("code" + phoneNumber, verificationCode);
        return verificationCode;
    }


    public String getVerificationCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int c = random.nextInt(10);
            code = code + c;
        }
        return code;
    }
}
