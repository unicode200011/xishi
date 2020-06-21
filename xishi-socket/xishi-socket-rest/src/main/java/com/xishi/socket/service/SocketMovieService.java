package com.xishi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;

public interface SocketMovieService {
    boolean doSockeMovie(SocketIOClient client, Long userId,Long movieId, String event);

    void updateMovieWatchNum(Long movieId, Integer roomClientNum,Integer io);
}
