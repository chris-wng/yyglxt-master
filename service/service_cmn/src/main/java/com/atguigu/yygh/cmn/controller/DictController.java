package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin   //解决跨域问题
public class DictController {

    @Autowired
    private DictService dictService;

    //根据dictCode获取下级节点
    @GetMapping("findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@ApiParam(name = "dictCode", value = "节点编码1", required = true)
                                             @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }


    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value ){
        String name = dictService.getNameByParentDictCodeAndValue(dictCode,value);
        return name;
    }

    @GetMapping("getName/{value}")
    public String getName(@PathVariable("value") String value){
        String name = dictService.getNameByParentDictCodeAndValue("",value);
        return name;
    }


    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChlidData(id);
        return Result.ok(list);
    }

    //导出数据字典接口
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);
    }

    //导入execl文件
    @PostMapping("importData")
    public void importData(MultipartFile file){
        dictService.importData(file);
    }

}
