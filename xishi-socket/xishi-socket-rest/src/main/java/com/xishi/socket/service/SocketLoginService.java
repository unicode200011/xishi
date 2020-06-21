package com.xishi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;

public interface SocketLoginService {

    boolean doSockeLogin(SocketIOClient client,Long userId,String event);
}
