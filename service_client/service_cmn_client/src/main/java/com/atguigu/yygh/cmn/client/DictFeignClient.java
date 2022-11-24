package com.atguigu.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
public interface DictFeignClient {
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}}")
    String getName(@PathVariable("dictCode") String parentDictCode,
                          @PathVariable("value") String value );
    @GetMapping("/admin/cmn/dict/getName/{value}")
     String getName(@PathVariable("value") String value);
}
