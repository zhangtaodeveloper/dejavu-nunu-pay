package com.dejavu.nunu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@MapperScan({"com.dejavu.nunu.system.*.mapper"})
@SpringBootApplication(scanBasePackages = {"com.dejavu.nunu.*"})
public class SystemApplication {


    public static void main(String[] args) {

        SpringApplication.run(SystemApplication.class, args);

    }



}
