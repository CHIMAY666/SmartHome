package edu.xmu.smarthome.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/test")  // 处理请求路径为/test的请求
    public String test(){
        return "";
    }
}
