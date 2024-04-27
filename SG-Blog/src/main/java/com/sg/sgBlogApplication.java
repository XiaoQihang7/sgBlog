package com.sg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableConfigurationProperties
//@ConfigurationPropertiesScan
@EnableScheduling
@MapperScan("com.sg.mapper")
@EnableSwagger2
@EnableAsync
public class sgBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(sgBlogApplication.class,args);
    }
}
