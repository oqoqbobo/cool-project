package com.oqoqbobo.web;

import cn.jy.operationLog.config.EnableOperationLog;
import cn.jy.operationLog.push2es.EnableOperationLogElasticSearchSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableOperationLogElasticSearchSupport
@EnableOperationLog //在启动类加上注解
@ComponentScan(basePackages = {"com.data","com.oqoqbobo.web"})
@MapperScan(basePackages = {"com.data.mapper", "com.oqoqbobo.web.mapper"})
@SpringBootApplication
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

}
