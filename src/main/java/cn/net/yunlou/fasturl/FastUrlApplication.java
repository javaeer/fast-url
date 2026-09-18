package cn.net.yunlou.fasturl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cn.net.yunlou.*.mapper"})
public class FastUrlApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastUrlApplication.class, args);
    }

}
