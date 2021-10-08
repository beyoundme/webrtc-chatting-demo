package com.beyoundme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: start
 **/
@SpringBootApplication(scanBasePackages = "com.beyoundme")
@EnableScheduling
public class Application {

    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

}

