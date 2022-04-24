package com.meta;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {
        "com.meta.authentication.store.mybatis"
})
@EnableCreateCacheAnnotation
public class AuthenticationCenterApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationCenterApiApplication.class, args);
    }

}
