package org.liko.project.liko_phone.controller;

import com.alibaba.fastjson.JSON;
import org.liko.project.liko_phone.entity.Sms;
import org.liko.project.liko_phone.listener.CommEventListener;
import org.liko.project.liko_phone.repository.SmsRepository;
import org.liko.project.liko_phone.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Liko
 * @Description:
 * @Date: Created at 19:18 2018/12/14
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private CommEventListener listener;

    @RequestMapping("/del/{id}")
    public void deleteSms(@PathVariable String id) {

    }

    @RequestMapping("/add")
    public void add() {
        Sms sms = new Sms("123", "ttt");
        smsRepository.save(sms);
    }

    @RequestMapping("/del")
    public String add(@Param("pwd") String pwd) {
        if ("likopwd".equals(pwd)) {
            for (int i = 0; i <= 100; i++) {
                try {
                    listener.sendAT("AT+CMGD=" + i +  ",0");
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "Success";
        } else {
            return "密码不正确, 请重新输入!";
        }
    }

    @RequestMapping("/{id}")
    public String getById(@PathVariable("id") Integer id) {
        String result = "success";
        try {
            listener.sendAT("AT+CMGR=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/show")
    public String get() {
        for (int i = 0; i <= 100; i++) {
            try {
                listener.sendAT("AT+CMGR=" + i);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}
