package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChlidData(Long id);
    //导出数据字典的数据
    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
    //根据dictCode和value查询
    String getNameByParentDictCodeAndValue(String dictCode, String value);
//根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);
}
