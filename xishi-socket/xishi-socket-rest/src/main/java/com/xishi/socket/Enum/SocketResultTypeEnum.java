package com.xishi.socket.Enum;

import lombok.Getter;

@Getter
public enum SocketResultTypeEnum {
    RESULT_LOGIN(0,"登录"),
    RESULT_WATCH_MOVIE(1,"看电影"),
    RESULT_QUIT_MOVIE(2,"退出看电影"),
    RESULT_ADD_LIVE(4,"加入直播间"),
    RESULT_MESSAGE_SEND(5,"发送直播间消息"),
    RESULT_MESSAGE_STOP(6,"发送直播间禁播消息"),
    RESULT_MESSAGE_WARN(7,"发送直播间直播警告消息"),
    RESULT_SHUTDOWN_LIVE(8,"结束直播"),
    RESULT_CHANGE_MODE_LIVE(9,"转换直播模式"),
    RESULT_GET_OUT_LIVE(10,"踢出房间"),
    RESULT_NO_SEND_LIVE(11,"禁言"),
    RESULT_ALL_NO_SEND_LIVE(12,"全员禁言"),
    RESULT_ADD_LIVE_MESSAGE(13,"用户加入直播间消息"),
    RESULT_CREATE_LIVE(3,"创建直播间"),;

    private Integer code;

    private String namespace;

    SocketResultTypeEnum(Integer code, String namespace) {
        this.code = code;
        this.namespace = namespace;
    }
}
