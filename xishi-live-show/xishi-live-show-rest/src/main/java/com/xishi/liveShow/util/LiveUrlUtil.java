package com.xishi.liveShow.util;

import com.xishi.liveShow.config.LiveConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author LX
 */
@Component
@Data
public class LiveUrlUtil {
    @Autowired
    private LiveConfig liveConfig;
    /**
     *  获取推流地址
     * @param streamName 流名
     * @return
     */
    public String genPushUrl(String streamName){
        StringBuilder url = new StringBuilder();
        long l = System.currentTimeMillis() / 1000;
        String safeUrl = LiveSafeUrlUtil.getSafeUrl(liveConfig.getPushKey(), streamName, l + liveConfig.getOverTime());
        String fullUrl = url.append(liveConfig.getPushName()).append(safeUrl).toString();
        return  fullUrl.replaceAll("STREAM_NAME",streamName);
    }

    /**
     *  获取RTMP播流地址
     * @param streamName 流名
     * @param type 播流类型 RTMP  FLV HLS
     * @return
     */
    public String genPullUrl(String streamName,String type){
        StringBuilder url = new StringBuilder();
        long l = System.currentTimeMillis() / 1000;
        String safeUrl = LiveSafeUrlUtil.getSafeUrl(liveConfig.getPullKey(), streamName, l + liveConfig.getOverTime());
        String fullUrl = "";
        switch (type){
            case "RTMP":{
                fullUrl = url.append(liveConfig.getPullRtmpName()).append(safeUrl).toString();
                break;
            }
            case "FLV":{
                fullUrl = url.append(liveConfig.getPullFLVName()).append(safeUrl).toString();
                break;
            }
            case "HLS":{
                fullUrl = url.append(liveConfig.getPullHLSName()).append(safeUrl).toString();
                break;
            }
        }
        return  fullUrl.replaceAll("STREAM_NAME",streamName);
    }
}
