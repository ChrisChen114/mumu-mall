package com.imooc.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mall.model.dao")    //加上这句话是给mybatis用的，而不是给IDEA等用的
@EnableSwagger2
@EnableCaching
public class ImoocMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImoocMallApplication.class, args);
    }

}
