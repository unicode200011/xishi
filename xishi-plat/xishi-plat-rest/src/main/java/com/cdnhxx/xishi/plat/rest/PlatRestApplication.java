package com.cdnhxx.xishi.plat.rest;

import com.cloud.webcore.ApplicationStarter;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: lx
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.cdnhxx.xishi.plat.rest.dao"})
public class PlatRestApplication {

    private static final Logger log = LoggerFactory.getLogger(PlatRestApplication.class);

    public static void main(String[] args) {
        ApplicationStarter.run(PlatRestApplication.class, args);
        log.info("PlatRestApplication is running!");
    }
}
