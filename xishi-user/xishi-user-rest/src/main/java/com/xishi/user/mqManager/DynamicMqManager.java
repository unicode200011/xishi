package com.xishi.user.mqManager;

import com.alibaba.fastjson.JSONObject;
import com.xishi.user.entity.Enum.MqQueueNameConstent;
import com.xishi.user.entity.req.DynamicReq;
import com.xishi.user.service.IDynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DynamicMqManager {

    @Autowired
    private IDynamicService dynamicService;

    /**
     *  处理动态点赞
     * @param dynamicMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.DYNAMIC_PRAISE)
    public void dynamic_praise(String dynamicMessage) {
        log.info("处理动态点赞-收到消息  : " + dynamicMessage);
        dynamicService.praiseDynamic(JSONObject.parseObject(dynamicMessage, DynamicReq.class));
    }

    /**
     *  发布动态
     * @param dynamicMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.DYNAMIC_SEND)
    public void dynamic_send(String dynamicMessage) {
        log.info("处理发布动态-收到消息  : " + dynamicMessage);
        dynamicService.sendDynamic(JSONObject.parseObject(dynamicMessage, DynamicReq.class));
    }

    /**
     *  删除动态
     * @param dynamicMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.DYNAMIC_DELETE)
    public void dynamic_delete(String dynamicMessage) {
        log.info("处理删除动态-收到消息  : " + dynamicMessage);
        dynamicService.deleteDynamic(JSONObject.parseObject(dynamicMessage, DynamicReq.class));
    }
}
