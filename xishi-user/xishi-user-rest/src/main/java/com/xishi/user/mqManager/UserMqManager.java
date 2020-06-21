package com.xishi.user.mqManager;

import com.alibaba.fastjson.JSONObject;
import com.xishi.user.entity.mqMessage.*;
import com.xishi.socket.entity.Enum.MqUserServiceTypeConstens;
import com.xishi.user.entity.Enum.MqQueueNameConstent;
import com.xishi.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UserMqManager {
    @Autowired
    private ILoginService loginService;

    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private IUserGradeService userGradeService;
    @Autowired
    private ILiveGradeService liveGradeService;
    @Autowired
    private IAgentWalletRecordService agentWalletRecordService;
    @Autowired
    private IAdminWalletRecordService adminWalletRecordService;
    /**
     *  处理用户相关
     * @param userMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.USER_QUEUE)
    public void userService(String userMessage) {
        log.info("处理用户相关-收到消息  : " + userMessage);
        UserRabbitMessage userRabbitMessage = JSONObject.parseObject(userMessage, UserRabbitMessage.class);
        String type = userRabbitMessage.getType();
        switch (type){
            case MqUserServiceTypeConstens.USER_LOGOUT://用户掉线
                log.info("处理用户掉线消息  : " + userMessage);
                loginService.offline(userRabbitMessage.getUserId());
                break;
            case MqUserServiceTypeConstens.USER_LOGIN://用户掉线
                log.info("处理用户上线消息  : " + userMessage);
                loginService.online(userRabbitMessage.getUserId());
                break;
            case MqUserServiceTypeConstens.USER_GRADE://增加用户等级
                log.info("处理增加用户等级消息  : " + userMessage);
                userGradeService.addUserGrade(userRabbitMessage);
                break;
            case MqUserServiceTypeConstens.LIVE_GRADE://增加直播等级
                log.info("处理增加直播等级消息  : " + userMessage);
                liveGradeService.addLiveGrade(userRabbitMessage);
                break;
        }
    }

    /**
     *  处理赠送礼物
     * @param liveGiftMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.LIVE_GIFT_QUEUE)
    public void liveGiftService(String liveGiftMessage) {
        log.info("处理赠送礼物-收到消息  : " + liveGiftMessage);
        LiveGiftRabbitMessage liveGiftRabbitMessage = JSONObject.parseObject(liveGiftMessage, LiveGiftRabbitMessage.class);
        userWalletService.sendGift(liveGiftRabbitMessage);
    }

    /**
     *  处理直播付费
     * @param livePayMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.USER_PAY_QUEUE)
    public void livePayService(String livePayMessage) {
        LivePayRabbitMessage livePayRabbitMessage = JSONObject.parseObject(livePayMessage, LivePayRabbitMessage.class);
        log.info("userMqManager-收到消息  : " + livePayMessage);
        if(livePayRabbitMessage.getLiveMode() == 2){ //更新计时收费 用户消费记录
            log.info("更新计时收费-收到消息  : " + livePayMessage);
            userWalletService.sendLivePayTimeCountRecord(livePayRabbitMessage);
        }else {
            log.info("处理直播付费-收到消息  : " + livePayMessage);
            userWalletService.sendLivePay(livePayRabbitMessage);
        }
    }
    /**
     *  处理看电影付费
     * @param moviePayMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.MOVIE_PAY_QUEUE)
    public void moviePayService(String moviePayMessage) {
        log.info("处理看电影付费-收到消息  : " + moviePayMessage);
        MoviePayRabbitMessage moviePayRabbitMessage = JSONObject.parseObject(moviePayMessage, MoviePayRabbitMessage.class);
        userWalletService.sendMoviePay(moviePayRabbitMessage);
    }
    /**
     *  处理代理商提成
     * @param moviePayMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.AGENT_RATE_MONEY_QUEUE)
    public void agentMoneyService(String moviePayMessage) {
        log.info("处理代理商提成-收到消息  : " + moviePayMessage);
        AgentMoneyRabbitMessage agentMoneyRabbitMessage = JSONObject.parseObject(moviePayMessage, AgentMoneyRabbitMessage.class);
        agentWalletRecordService.agentMoneyService(agentMoneyRabbitMessage);
    }

    /**
     *  处理平台提成
     * @param moviePayMessage
     */
    @RabbitListener(queues = MqQueueNameConstent.ADMIN_RATE_MONEY_QUEUE)
    public void adminMoneyService(String moviePayMessage) {
        log.info("处理平台提成-收到消息  : " + moviePayMessage);
        AdminMoneyRabbitMessage adminMoneyRabbitMessage = JSONObject.parseObject(moviePayMessage, AdminMoneyRabbitMessage.class);
        adminWalletRecordService.adminMoneyService(adminMoneyRabbitMessage);
    }

}
