package cn.net.yunlou.fasturl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.net.yunlou.*"})
@MapperScan(basePackages = {"cn.net.yunlou.*.mapper"})
public class FastUrlApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastUrlApplication.class, args);
    }

}
