package com.xishi.liveShow.mqManager;

import com.alibaba.fastjson.JSONObject;
import com.xishi.liveShow.entity.constens.MqLiveServiceTypeConstens;
import com.xishi.liveShow.service.ILiveService;
import com.xishi.user.entity.mqMessage.LiveRabbitMessage;
import com.xishi.user.entity.Enum.MqQueueNameConstent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LiveMqManager {
    @Autowired
    private ILiveService liveService;
    /**
     *  处理结束直播
     * @param userMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.LIVE_QUEUE)
    public void userService(String userMessage) {
        log.info("处理结束直播-收到消息  : " + userMessage);
        LiveRabbitMessage liveRabbitMessage = JSONObject.parseObject(userMessage, LiveRabbitMessage.class);
        String type = liveRabbitMessage.getType();
        switch (type){
            case MqLiveServiceTypeConstens.LIVE_DISCONNECT:
                liveService.discBack(liveRabbitMessage.getStreamName());
                break;
        }
    }

}
