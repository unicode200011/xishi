package com.xishi.socket.controller;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.socket.entity.req.PushMessage;
import com.xishi.socket.feign.UserService;
import com.xishi.socket.netty_socketio.service.SocketIOService;
import com.xishi.user.entity.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socket")
public class socketController {

    @Autowired
    SocketIOService socketIOService;
    @Autowired
    private UserService userService;

    @GetMapping("/getMessage")
    public String getMessage(Long userId){
        Req<Long> userReq = new Req<Long>(userId);
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(userReq);
        UserInfoVo data = userInfoVoResp.getData();
        PushMessage pushMessage = new PushMessage();
        pushMessage.setLoginUserId(data.getUserId());
        socketIOService.pushMessageToUser(pushMessage,"");
        return data.getName();
    }

}
