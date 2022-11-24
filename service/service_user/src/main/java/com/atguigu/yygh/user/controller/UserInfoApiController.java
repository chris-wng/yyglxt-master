package com.atguigu.yygh.user.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "会员登陆")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.login(loginVo);
        return Result.ok(map);
    }

    @ApiOperation(value = "短信")
    @GetMapping("send/{phone}/{validateTime}")
    public Result send(@PathVariable String phone
            ,@PathVariable String validateTime){
        userInfoService.send(phone,validateTime);
        return Result.ok();
    }

}
