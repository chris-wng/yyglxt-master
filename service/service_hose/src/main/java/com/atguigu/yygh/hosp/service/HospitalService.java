package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> paramMap);
    Hospital getByHoscode(String hoscode);

    Page<Hospital> getHosplist(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String,Object> show(String id);

    String getHospName(String hoscode);

    //根据医院名称模糊查询
    List<Hospital> findByHosname(String hosname);
    //医院预约挂号详细
    Map<String, Object> item(String hoscode);
}
