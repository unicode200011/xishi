package com.xishi.socket.netty_socketio.service;


import com.xishi.socket.entity.req.PushMessage;

public interface SocketIOService {
    //推送的事件
    public static final String PUSH_ALL = "push_all";
    public static final String SOCKET_AD_UPDATE = "socket_ad_update";
    public static final String PUSH_SINGLE = "push_single";
    public static final String SOCKET_LOGIN = "socket_login:";
    public static final String SOCKET_RESULT = "socket_result";
    public static final String STOP_USER = "stop_user";

    public static final String SOCKET_MOVIE = "socket_movie:";
    public static final String SOCKET_MOVIE_DISCONNECT = "socket_movie_disconnect:";
    public static final String SOCKET_LIVE = "socket_live:";
    public static final String SOCKET_LIVE_DISCONNECT = "socket_live_disconnect:";
    // 启动服务
    void start() throws Exception;

    // 停止服务
    void stop();

    // 推送信息
    void pushMessageToUser(PushMessage pushMessage,String type);
}
