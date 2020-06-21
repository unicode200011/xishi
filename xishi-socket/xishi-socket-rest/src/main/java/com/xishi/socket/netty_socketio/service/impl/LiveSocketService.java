package com.xishi.socket.netty_socketio.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.cloud.webcore.service.RedisService;
import com.common.base.util.TransUtil;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.xishi.socket.Enum.SocketResultTypeEnum;
import com.xishi.socket.entity.req.*;
import com.xishi.socket.entity.vo.SocketLiveCurrentDataVo;
import com.xishi.socket.entity.vo.SocketResultVo;
import com.xishi.socket.entity.vo.SocketUserVo;
import com.xishi.socket.service.SocketLiveService;
import com.xishi.user.entity.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class LiveSocketService {
    //创建直播间
    private static final String SOCKET_LIVE_CREATE="socket_live_create:";
    //结束直播
    private static final String SOCKET_LIVE_SHUTDOWN="socket_live_shutdown:";
    //装换直播模式
    private static final String SOCKET_LIVE_CHANGE_MODE="socket_live_change_mode:";
    //加入直播间
    private static final String SOCKET_LIVE_ADD="socket_live_add:";
    //退出直播间
    private static final String SOCKET_LIVE_QUIT="socket_live_quit:";
    //发送直播间消息
    private static final String SOCKET_LIVE_MESSAGE_SEND="socket_live_message_send:";
    //接收直播间消息
    private static final String SOCKET_LIVE_MESSAGE="socket_live_message";

    //触发直播间全员禁言
    private static final String SOCKET_STOP_SEND_MESSAGE="socket_stop_send_message";
    //发送直播间礼物
    private static final String SOCKET_LIVE_GIFT="socket_live_gift:";
    //发送直播禁播消息
    private static final String SOCKET_LIVE_STOP="socket_live_stop:";
    //发送直播警告消息
    private static final String SOCKET_LIVE_WARN="socket_live_warn:";
    //触发计时收费
    private static final String SOCKET_LIVE_PAY="socket_live_pay:";
    //触发踢出房间
    private static final String SOCKET_LIVE_GETOUT="socket_live_getout:";
    //触发禁言
    private static final String SOCKET_LIVE_NOSEND="socket_live_nosend:";
    //接收直播间礼物消息
    private static final String SOCKET_LIVE_GIFT_MESSAGE="socket_live_gift_message";
    //接收加入直播间消息
    private static final String SOCKET_ADD_LIVE_MESSAGE="socket_add_live_message";

    //socket推送消息
    public static final String SOCKET_RESULT = "socket_result";

    //socket推送实时更新消息
    public static final String SOCKET_CURRENT_RESULT = "socket_current_result";

    // 用来存已连接的直播
    private Map<String, SocketIOClient> clientMap;
    //
    private Map<String, SocketIOClient> userClientMap;
    private SocketIOServer socketIOServer;
    private SocketLiveCurrentDataVo socketLiveCurrentDataVo;
    private SocketLiveService socketLiveService;
    private RedisService redisService;

    LiveSocketService(SocketIOServer socketIOServer, Map<String, SocketIOClient> clientMap, SocketLiveService socketLiveService, SocketLiveCurrentDataVo socketLiveCurrentDataVo, RedisService redisService, Map<String, SocketIOClient> userClientMap){
        this.socketIOServer = socketIOServer;
        this.clientMap = clientMap;
        this.userClientMap = userClientMap;
        this.socketLiveService = socketLiveService;
        this.socketLiveCurrentDataVo = socketLiveCurrentDataVo;
        this.redisService = redisService;
        initLiveListener();
    }

    public void initLiveListener(){
        //主播开启直播 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_CREATE, SocketLiveReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】开启直播:ThreadName:{}" ,SOCKET_LIVE_CREATE, Thread.currentThread().getName());
            log.info("事件【{}】开启直播:data={}",SOCKET_LIVE_CREATE,data);
            String streamName = data.getStreamName();
            clientMap.put(streamName,client);
            String roomId = SOCKET_LIVE_ADD + streamName;
            client.joinRoom(roomId);//加入直播房间
            client.sendEvent(SOCKET_RESULT, SocketResultVo.success(SocketResultTypeEnum.RESULT_CREATE_LIVE.getCode()));
        });

        //主播结束直播 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_SHUTDOWN, SocketLiveReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】结束直播:ThreadName:{}" ,SOCKET_LIVE_SHUTDOWN, Thread.currentThread().getName());
            log.info("事件【{}】结束直播:data={}",SOCKET_LIVE_SHUTDOWN,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT,SocketResultVo.success(SocketResultTypeEnum.RESULT_SHUTDOWN_LIVE.getCode()));

            //发送结束直播MQ消息
            if(data.getLiveMode() == 2) {
                socketLiveService.doEndLive(data);
            }
        });

        //主播转换直播模式 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_CHANGE_MODE, SocketLiveChangeModeReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】转换直播模式:ThreadName:{}" ,SOCKET_LIVE_CHANGE_MODE, Thread.currentThread().getName());
            log.info("事件【{}】转换直播模式:data={}",SOCKET_LIVE_CHANGE_MODE,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT,SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_CHANGE_MODE_LIVE.getCode()));
        });

        //主播通知全员禁言 ：  接口禁言 这里只推送禁言/取消禁言消息
        socketIOServer.addEventListener(SOCKET_STOP_SEND_MESSAGE, SocketLiveChangeModeReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】全员禁言:ThreadName:{}" ,SOCKET_STOP_SEND_MESSAGE, Thread.currentThread().getName());
            log.info("事件【{}】全员禁言:data={}",SOCKET_STOP_SEND_MESSAGE,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_STOP_SEND_MESSAGE,data);
        });

        //触发直播计时收费 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_PAY, SocketLivePayReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】计时收费:ThreadName:{}" ,SOCKET_LIVE_PAY, Thread.currentThread().getName());
            log.info("事件【{}】计时收费:data={}",SOCKET_LIVE_PAY,data);
            log.info("进来了");
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            socketLiveService.doTimeCount(data,roomId,client);
        });

        //触发踢出房间 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_GETOUT, SocketLivePayReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】踢出房间:ThreadName:{}" ,SOCKET_LIVE_GETOUT, Thread.currentThread().getName());
            log.info("事件【{}】踢出房间:data={}",SOCKET_LIVE_GETOUT,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            socketLiveService.doGetOut(data,roomId);

            //TODO 全房间推送  触发踢出房间
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT,SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_GET_OUT_LIVE.getCode()));
            if(data.getLiveMode() == 3){
                //计时收费模式 扣除相应费用
                socketLiveService.doPayTimeCount(data,roomId);
            }
        });

        //触发禁言 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_NOSEND, SocketLivePayReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】禁言:ThreadName:{}" ,SOCKET_LIVE_NOSEND, Thread.currentThread().getName());
            log.info("事件【{}】禁言:data={}",SOCKET_LIVE_NOSEND,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            socketLiveService.doNoSend(data,roomId);
            //TODO 全房间推送  触发禁言
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT,SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_NO_SEND_LIVE.getCode()));
        });

        //用户加入直播 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_ADD, SocketLiveReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】加入直播间:ThreadName:{}" ,SOCKET_LIVE_ADD, Thread.currentThread().getName());
            log.info("事件【{}】加入直播间:data={}",SOCKET_LIVE_ADD,data);
            log.info("json:"+ JSONObject.toJSONString(data));

            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            client.joinRoom(roomId);//加入直播房间
            userClientMap.put(data.getUserId()+"",client);
            //查询对应房间连接数
            Integer roomClientNum = getRoomClientNum(roomId);
            //更新对应直播在线观看人数
            SocketLiveCurrentDataVo socketLiveCurrentDataVo = socketLiveService.updateLiveWatchNum(data, roomClientNum - 1, 0);
            client.sendEvent(SOCKET_RESULT, SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_ADD_LIVE.getCode()));
            //TODO 全房间推送 实时更新--推送直播详情--在线人数
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_CURRENT_RESULT, socketLiveCurrentDataVo);
            roomOperations.sendEvent(SOCKET_ADD_LIVE_MESSAGE, data);
        });

        //用户退出直播 监听器
        socketIOServer.addEventListener(SOCKET_LIVE_QUIT, SocketLiveReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】退出直播间:ThreadName:{}" ,SOCKET_LIVE_QUIT, Thread.currentThread().getName());
            log.info("事件【{}】退出直播间:data={}",SOCKET_LIVE_QUIT,data);
            String streamName = data.getStreamName();
            String roomId = SOCKET_LIVE_ADD + streamName;
            client.leaveRoom(roomId);//加入直播房间
            //查询对应房间连接数
            Integer roomClientNum = getRoomClientNum(roomId);
            //更新对应直播在线观看人数
            SocketLiveCurrentDataVo socketLiveCurrentDataVo = socketLiveService.updateLiveWatchNum(data, roomClientNum - 1, 1);
            if(data.getLiveMode()!=null && data.getLiveMode() == 3){
                //计时收费模式 扣除相应费用
                SocketLivePayReq socketLivePayReq = TransUtil.transEntity(data, SocketLivePayReq.class);
                socketLiveService.doPayTimeCount(socketLivePayReq,roomId);
            }
            //TODO 全房间推送 实时更新--推送直播详情--在线人数
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_CURRENT_RESULT, socketLiveCurrentDataVo);
        });

        //监听 发送直播间消息
        socketIOServer.addEventListener(SOCKET_LIVE_MESSAGE_SEND, SocketLiveSendMessageReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】发送直播间消息:ThreadName:{}" ,SOCKET_LIVE_MESSAGE_SEND, Thread.currentThread().getName());
            log.info("事件【{}】发送直播间消息:data={}",SOCKET_LIVE_MESSAGE_SEND,data);
            String streamName = data.getStreamName();
            String roomId=SOCKET_LIVE_ADD + streamName;
            //查询对应房间连接数
            Integer roomClientNum = getRoomClientNum(roomId);
            boolean level = socketLiveService.checkUserLevel(data,roomClientNum);
            boolean b = socketLiveService.checkNoSend(data);
            boolean all = socketLiveService.checkAllNoSend(data);
            if(all){
                client.sendEvent(SOCKET_RESULT, SocketResultVo.error("房间已全体禁言",SocketResultTypeEnum.RESULT_MESSAGE_SEND.getCode()));
            }else if(!level){
                client.sendEvent(SOCKET_RESULT, SocketResultVo.error("等级不够不能发言",SocketResultTypeEnum.RESULT_MESSAGE_SEND.getCode()));
            } else{
                if(!b){
                    //检查用户信息
                    socketLiveService.checkUserInfo(data);
                    client.sendEvent(SOCKET_RESULT, SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_MESSAGE_SEND.getCode()));
                    //全房间推送
                    BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
                    roomOperations.sendEvent(SOCKET_LIVE_MESSAGE,data);
                }else {
                    client.sendEvent(SOCKET_RESULT, SocketResultVo.error("你已被禁言",SocketResultTypeEnum.RESULT_MESSAGE_SEND.getCode()));
                }
            }

        });

        //监听 发送礼物
        socketIOServer.addEventListener(SOCKET_LIVE_GIFT, SocketLiveSendGiftReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】发送直播间礼物:ThreadName:{}" ,SOCKET_LIVE_GIFT, Thread.currentThread().getName());
            log.info("事件【{}】发送直播间礼物:data={}",SOCKET_LIVE_GIFT,data);
            String streamName = data.getStreamName();
            String roomId=SOCKET_LIVE_ADD + streamName;
            //全房间推送 赠送礼物消息
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_LIVE_GIFT_MESSAGE,data);
            client.sendEvent(SOCKET_RESULT, SocketResultVo.success(SocketResultTypeEnum.RESULT_MESSAGE_SEND.getCode()));
            //TODO 全房间推送 实时更新--推送直播详情--贡献值
            SocketLiveCurrentDataVo socketLiveCurrentDataVo = socketLiveService.sendGift(data);
            log.info("事件【{}】发送直播间礼物:data={}","socketLiveCurrentDataVo",socketLiveCurrentDataVo);
            roomOperations.sendEvent(SOCKET_CURRENT_RESULT, socketLiveCurrentDataVo);
        });
        //监听 发送直播禁播
        socketIOServer.addEventListener(SOCKET_LIVE_STOP, SocketLiveStopReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】发送直播禁播:ThreadName:{}" ,SOCKET_LIVE_STOP, Thread.currentThread().getName());
            log.info("事件【{}】发送直播禁播:data={}",SOCKET_LIVE_STOP,data);
            String streamName = data.getStreamName();
            String roomId=SOCKET_LIVE_ADD + streamName;
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT, SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_MESSAGE_STOP.getCode()));
        });
        //监听 发送直播警告消息
        socketIOServer.addEventListener(SOCKET_LIVE_WARN, SocketLiveStopReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】发送警告消息:ThreadName:{}" ,SOCKET_LIVE_WARN, Thread.currentThread().getName());
            log.info("事件【{}】发送警告消息:data={}",SOCKET_LIVE_WARN,data);
            String streamName = data.getStreamName();
            String roomId=SOCKET_LIVE_ADD + streamName;
            BroadcastOperations roomOperations = socketIOServer.getNamespace("").getRoomOperations(roomId);
            roomOperations.sendEvent(SOCKET_RESULT, SocketResultVo.successData(data,SocketResultTypeEnum.RESULT_MESSAGE_WARN.getCode()));
        });

    }

    private Integer getRoomClientNum(String roomId){
        log.info("查询直播间{}的人数",roomId);
        BroadcastOperations loginRoom = this.socketIOServer.getNamespace("").getRoomOperations(roomId);
        Collection<SocketIOClient> clients = loginRoom.getClients();
        log.info("直播间{}的人数是【{}】",roomId,clients.size());
        return clients.size();
    }



}
