package com.atguigu.yygh.user.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.SmsSendUtils;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

     @Autowired
    SmsSendUtils smsSendUtils;
    //会员登陆
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        //对loginVo对象的手机号和code进行判断是否存在
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //TODO 判断手机验证码和输入的验证码是否一致
        String send = send(phone, "1");
        if(!code.equals(send)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //进入数据库查看手机号是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //获取会员
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        //如果不存在则进行注册添加
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            userInfo.setName("");
            this.save(userInfo);
        }
        //对用户进行判断是否杯禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //TODO 记录登录


        //返回页面记录名称
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    //发送短信
    @Override
    public String send(String phone, String validateTime) {
        String s = smsSendUtils.smsSend(phone, validateTime);
        return s;
    }

}
