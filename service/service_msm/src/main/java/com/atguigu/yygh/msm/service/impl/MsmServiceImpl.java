package com.atguigu.yygh.msm.service.impl;

import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.SmsSendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    SmsSendUtils smsSendUtils;

    @Override
    public void send(String phone, String validateTime) {
        smsSendUtils.smsSend(phone,validateTime);
    }
}
