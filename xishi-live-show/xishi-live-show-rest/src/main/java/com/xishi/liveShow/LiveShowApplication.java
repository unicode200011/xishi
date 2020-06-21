package com.xishi.liveShow;

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
@MapperScan(value = "com.xishi.liveShow.dao")
public class LiveShowApplication {
    private final static Logger logger = LoggerFactory.getLogger(LiveShowApplication.class);
    public static void main(String[] args) {
        ApplicationStarter.run(LiveShowApplication.class, args);
        logger.info("LiveShowApplication start success......................");
    }
}
