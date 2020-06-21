package com.xishi.movie;

import com.cloud.webcore.ApplicationStarter;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(value = "com.xishi.movie.dao")
public class MovieApplication {
    private final static Logger logger = LoggerFactory.getLogger(MovieApplication.class);
    public static void main(String[] args) {
        ApplicationStarter.run(MovieApplication.class, args);
        logger.info("MovieApplication start success......................");
    }
}
