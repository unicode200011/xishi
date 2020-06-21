package com.xishi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.xishi.socket.entity.req.SocketLivePayReq;
import com.xishi.socket.entity.req.SocketLiveReq;
import com.xishi.socket.entity.req.SocketLiveSendGiftReq;
import com.xishi.socket.entity.req.SocketLiveSendMessageReq;
import com.xishi.socket.entity.vo.SocketLiveCurrentDataVo;

public interface SocketLiveService {
    SocketLiveCurrentDataVo updateLiveWatchNum(SocketLiveReq data, Integer roomClientNum, int i);

    void doGetOut(SocketLivePayReq data, String roomId);

    void doNoSend(SocketLivePayReq data, String roomId);

    boolean checkNoSend(SocketLiveSendMessageReq data);

    void doTimeCount(SocketLivePayReq data, String roomId, SocketIOClient client);

    void doPayTimeCount(SocketLivePayReq data, String roomId);

    void doEndLive(SocketLiveReq data);

    boolean checkAllNoSend(SocketLiveSendMessageReq data);

    SocketLiveCurrentDataVo sendGift(SocketLiveSendGiftReq data);

    void checkUserInfo(SocketLiveSendMessageReq data);

    boolean checkUserLevel(SocketLiveSendMessageReq data, Integer roomClientNum);
}
