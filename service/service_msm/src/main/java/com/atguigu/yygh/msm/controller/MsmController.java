package com.atguigu.yygh.msm.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.msm.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/msm")
@Api(tags = "发送短信")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @ApiOperation(value = "短信")
    @GetMapping("send/{phone}/{validateTime}")
    public Result send(@PathVariable String phone
                       ,@PathVariable String validateTime){
        msmService.send(phone,validateTime);
        return Result.ok();
    }
}
