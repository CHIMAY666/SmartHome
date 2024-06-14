package edu.xmu.smarthome.controller;

import edu.xmu.smarthome.mapper.TestMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    public TestController(TestMapper mapper) {
        this.mapper = mapper;
    }
    TestMapper mapper;
    @RequestMapping("/test")  // 处理请求路径为/test的请求
    public Object test(){
        return mapper.getAllDevices();
    }
}
