package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数对象转换成department对象
        String mapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(mapString, Department.class);
        //对department中的科室编号进行判断是否存在
        String depcode = department.getDepcode();
        String hoscode = department.getHoscode();
        Department departmentExit = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        //为空则不存在，进行添加，
        if(StringUtils.isEmpty(departmentExit)){
          department.setCreateTime(new Date());
          department.setIsDeleted(0);
          department.setUpdateTime(new Date());
          departmentRepository.save(department);
        }//不为空则存在，进行修改
        else{
            department.setCreateTime(department.getCreateTime());
            department.setIsDeleted(0);
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //创建一个Pageable对象,0为第一页
        Pageable pageable = (Pageable) PageRequest.of(page-1,limit,sort);

        //将deparementvo对象转换成department对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //创建匹配器，如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()//创建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//改变默认字符
                .withIgnoreCase(true);//改变默认大小写忽略方式：忽略大小写
        //创建实例
        Example<Department> example = Example.of(department,matcher);
        Page<Department> pages = departmentRepository.findAll(example,pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        //根据医院编号和科室编号取出科室对象
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,hoscode);
        //对是否存在对象进行判断
        if(department != null){
           departmentRepository.deleteById(department.getId());
        }
    }

    //根据医院编号，查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建一个list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号，查询所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        //所有科室列表 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);
        //根据大科室编号，bigcode分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合departmentMap
        for(Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            //大科室编号
            String bigcode = entry.getKey();
            //大科室所有信息
            List<Department> department1List = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(department1List.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for(Department department : department1List){
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //封装到list集合中
                children.add(departmentVo2);
            }
            departmentVo1.setChildren(children);
            result.add(departmentVo1);
        }
        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null){
            return department.getDepname();
        }
        return null;
    }
}
