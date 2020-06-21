package com.xishi.liveShow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@Component
@Profile(value = {"local", "test", "prod"})
@ConfigurationProperties(prefix = "live")
public class LiveConfig {
    private String pullName;
    private String pushName;
    private String pushKey;
    private String pullKey;
    private Long overTime;

    public String getPullRtmpName(){
        //RTMP
        return "rtmp://"+getPullName()+"/live/STREAM_NAME?";
    }

    public String getPullFLVName(){
        //FLV
        return "http://"+getPullName()+"/live/STREAM_NAME.flv?";
    }

    public String getPullHLSName(){
        //HLS
        return "http://"+getPullName()+"/live/STREAM_NAME.m3u8?";
    }
}
