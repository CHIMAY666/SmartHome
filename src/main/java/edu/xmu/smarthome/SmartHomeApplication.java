package edu.xmu.smarthome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.xmu.smarthome.mapper")
public class SmartHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeApplication.class, args);
    }

}
