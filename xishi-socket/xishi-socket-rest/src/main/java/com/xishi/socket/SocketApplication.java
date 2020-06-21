package com.xishi.socket;

import com.cloud.webcore.ApplicationStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SocketApplication {
    private final static Logger logger = LoggerFactory.getLogger(SocketApplication.class);
    public static void main(String[] args) {
        ApplicationStarter.run(SocketApplication.class, args);
        logger.info("SocketApplication start success......................");
    }
}
