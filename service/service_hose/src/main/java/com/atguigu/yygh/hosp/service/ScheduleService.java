package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> switchMap);
    Page<Schedule> getList(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    ////根据医院编号 、科室编号和工作日期，查询排班详细信息
    List<Schedule> getScheduleList(String hoscode, String depcode, String workDate);
}
