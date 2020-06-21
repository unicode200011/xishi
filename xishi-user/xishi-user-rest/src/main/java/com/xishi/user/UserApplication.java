package com.xishi.user;

import com.cloud.webcore.ApplicationStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserApplication {

    private final static Logger logger = LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args) {
        ApplicationStarter.run(UserApplication.class, args);
        logger.info("UserApplication start success......................");
    }
}
