package com.xishi.socket.service.impl;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.corundumstudio.socketio.SocketIOClient;
import com.xishi.socket.Enum.SocketResultTypeEnum;
import com.xishi.socket.entity.Enum.MqUserServiceTypeConstens;
import com.xishi.socket.entity.vo.SocketResultVo;
import com.xishi.socket.feign.UserService;
import com.xishi.socket.mq.MqSender;
import com.xishi.socket.service.SocketLoginService;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.entity.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketLoginServiceImpl implements SocketLoginService {

    @Autowired
    UserService userService;
    @Autowired
    MqSender mqSender;
    /**
     * 检测登录
     *
     * @param client
     * @param userId
     * @param event
     * @return
     */
    @Override
    public boolean doSockeLogin(SocketIOClient client, Long userId, String event) {
        Req<Long> userReq = new Req<Long>(userId);
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(userReq);
        UserInfoVo data = userInfoVoResp.getData();

        if (userInfoVoResp.getCode() != 200 || data == null || data.getState() != 0) {
            client.sendEvent(event, SocketResultVo.error("用户校验失败,用户不存在或状态异常", SocketResultTypeEnum.RESULT_LOGIN.getCode()));//单发
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.disconnect();//断开连接
            return false;
        }
        UserRabbitMessage userRabbitMessage = new UserRabbitMessage();
        userRabbitMessage.setType(MqUserServiceTypeConstens.USER_LOGIN);
        userRabbitMessage.setUserId(userId);
        mqSender.sendUserLoginMessage(userRabbitMessage);
        return true;
    }
}
