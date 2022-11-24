package com.atguigu.yygh.msm.service;

public interface MsmService {
    //发送短信验证码
    void send(String phone,String validateTime);
}
