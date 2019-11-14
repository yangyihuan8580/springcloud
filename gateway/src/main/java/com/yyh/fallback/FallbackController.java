package com.yyh.fallback;

import com.yyh.common.base.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/error")
public class FallbackController {

    @RequestMapping("/defaultfallback")
    public Map<String,String> fallback() {
        Map<String,String> result = new HashMap<>();
        result.put("code","9999");
        result.put("msg","系统繁忙，请稍后再试");
        return result;
    }
}
