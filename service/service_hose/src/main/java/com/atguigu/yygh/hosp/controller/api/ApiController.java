package com.atguigu.yygh.hosp.controller.api;
import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Map;


@Api(tags = "医院管理api接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) switchMap.get("hoscode");
        String hosScheduleId = (String) switchMap.get("hosScheduleId");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

    @ApiOperation(value = "获取排班信息")
    @PostMapping("schedule/list")
    public Result scheduleList(HttpServletRequest request){
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode =(String) switchMap.get("hoscode");
        String depcode = (String) switchMap.get("depcode");
        int page = StringUtils.isEmpty(switchMap.get("page")) ? 1 :Integer.parseInt((String)switchMap.get("page"));
        int limit = StringUtils.isEmpty(switchMap.get("limit")) ? 10 :Integer.parseInt((String) switchMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.getList(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "上传排班信息")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //将传递过来的数据封装到一个map对象中
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) switchMap.get("hoscode");
        //对医院编号进行判断
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //对签名进行校验
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(switchMap);
        return Result.ok();
    }

    @ApiOperation(value = "删除科室信息")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)switchMap.get("hoscode");
        String depcode = (String)switchMap.get("depcode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }


    @ApiOperation(value = "获取科室信息")
    @PostMapping("department/list")
    public Result departmentList(HttpServletRequest request){
        //将传递过来的医院信息
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) switchMap.get("hoscode");
        String depcode = (String) switchMap.get("depcode");
        int page = StringUtils.isEmpty(switchMap.get("page")) ? 1 : Integer.parseInt((String)switchMap.get("page"));
        int limit = StringUtils.isEmpty(switchMap.get("limit")) ? 10 : Integer.parseInt((String)switchMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);
        Page<Department> pageList = departmentService.selectPage(page,limit,departmentQueryVo);
        return Result.ok(pageList);
    }


    @ApiOperation(value = "上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //调用方法将医院平台提交的数据封装到一个map中
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        //1.获取医院系统传递过来的医院签名，签名进行md5加密
        String hospSign = (String)paramMap.get("sign");

        //2.获取医院系统传递过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.对signkey进行md5加密
        String signKeyMd5 = MD5.encrypt(signKey);

        //对两个密钥进行比对
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //添加save方法添加到医院中
        departmentService.save(paramMap);
        return Result.ok();
    }


    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //调用方法将医院平台提交的数据封装到一个map中
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        //1.获取医院系统传递过来的医院签名，签名进行md5加密
        String hospSign = (String)paramMap.get("sign");

        //2.获取医院系统传递过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.对signkey进行md5加密
        String signKeyMd5 = MD5.encrypt(signKey);

        //对两个密钥进行比对
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        //调用save方法将医院上传到预约平台
        hospitalService.save(paramMap);
        return Result.ok();
    }


    //查询医院接口
    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request){
        //调用方法将医院平台提交的数据封装到一个map对象中
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //取出hoscode并判断该编号是否为空，不为空则判断密钥
        String hoscode = (String)switchMap.get("hoscode");

        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(HttpRequestHelper.isSignEquals(switchMap,hospitalSetService.getSignKey(hoscode))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return Result.ok(hospitalService.getByHoscode((String)switchMap.get("hoscode")));
    }
}
