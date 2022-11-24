package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //获取医院信息
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                            HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pages = hospitalService.getHosplist(page,limit,hospitalQueryVo);
        return Result.ok(pages);
    }

    //更新上线状态
    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result lock(@PathVariable("id") String id,
                       @PathVariable("status") Integer status){
            hospitalService.updateStatus(id,status);
            return Result.ok();
    }

    //获取医院详细
    @ApiOperation(value = "获取医院详细")
    @GetMapping("show/{id}")
    public Result show(@PathVariable("id") String id){
        return Result.ok(hospitalService.show(id));
    }
}
