package com.xishi.socket.netty_socketio.service.impl;

import com.cloud.webcore.service.RedisService;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.xishi.socket.Enum.SocketResultTypeEnum;
import com.xishi.socket.entity.Enum.MqUserServiceTypeConstens;
import com.xishi.socket.entity.req.PushMessage;
import com.xishi.socket.entity.req.SocketLoginReq;
import com.xishi.socket.entity.req.SocketMovieReq;
import com.xishi.socket.entity.vo.SocketLiveCurrentDataVo;
import com.xishi.socket.entity.vo.SocketResultVo;
import com.xishi.socket.service.SocketLiveService;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.socket.mq.MqSender;
import com.xishi.socket.netty_socketio.service.SocketIOService;
import com.xishi.socket.service.SocketLoginService;
import com.xishi.socket.service.SocketMovieService;
import com.xishi.socket.util.StrKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {
    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    // 用来存已连接的直播
    private static Map<String, SocketIOClient> clientLiveMap = new ConcurrentHashMap<>();
    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private SocketLoginService socketLoginService;
    @Autowired
    private SocketMovieService socketMovieService;
    @Autowired
    private SocketLiveService socketLiveService;
    @Autowired
    private MqSender mqSender;
    @Autowired
    RedisService redisService;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }
    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception  {
        stop();
    }
    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userId = getParamsByClient(client);
            if (userId != null) {
                log.info("用户id【{}】连接到服务器....",userId);
                clientMap.put(userId, client);
            }
//            client.sendEvent(SOCKET_RESULT, SocketResultVo.success(RESULT_TYPE_OTHER));
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            this.listenDis(client); //监听连接断开  更新相关数量
//            String userId = getParamsByClient(client);
//            if (userId != null) {
//                log.info("用户id【{}】断开连接....",userId);
//                clientMap.remove(userId);
//                client.disconnect();
//            }
        });
        // 监听客户端群发推送
        socketIOServer.addEventListener(PUSH_ALL, PushMessage.class, (client, data, ackSender) -> {
            // TODO do something
            log.info("事件【{}】客户端群发:ThreadName:{}" ,PUSH_ALL, Thread.currentThread().getName());
            log.info("事件【{}】客户端群发:data={}",PUSH_ALL,data);
            this.pushAllUsers(data);
        });
        //监听客户端点对点推送
        socketIOServer.addEventListener(PUSH_SINGLE, PushMessage.class, (client, data, ackSender) -> {
            log.info("事件【{}】客户端单发:ThreadName:{}" ,PUSH_SINGLE, Thread.currentThread().getName());
            log.info("事件【{}】客户端单发:data={}",PUSH_SINGLE,data);
            pushMessageToUser(data,PUSH_SINGLE);//单发
        });
        //监听 后台更新广告
        socketIOServer.addEventListener(SOCKET_AD_UPDATE, PushMessage.class, (client, data, ackSender) -> {
            log.info("事件【{}】客户端单发:ThreadName:{}" ,SOCKET_AD_UPDATE, Thread.currentThread().getName());
            log.info("事件【{}】客户端单发:data={}",SOCKET_AD_UPDATE,data);
            this.socketIOServer.getBroadcastOperations().sendEvent(SOCKET_AD_UPDATE, data);
        });

        socketIOServer.addEventListener(SOCKET_LOGIN, SocketLoginReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】客户端登录:ThreadName:{}" ,SOCKET_LOGIN, Thread.currentThread().getName());
            log.info("事件【{}】客户端登录:data={}",SOCKET_LOGIN,data);
            //登陆前检测
            boolean b = socketLoginService.doSockeLogin(client, data.getUserId(),SOCKET_RESULT);
            if(b){
                Long loginUserId = data.getUserId();
                client.sendEvent(SOCKET_RESULT,SocketResultVo.success(SocketResultTypeEnum.RESULT_LOGIN.getCode()));
                clientMap.put(loginUserId.toString(),client);
            }
        });

        //监听播放电影
        socketIOServer.addEventListener(SOCKET_MOVIE, SocketMovieReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】播放电影:ThreadName:{}" ,SOCKET_MOVIE, Thread.currentThread().getName());
            log.info("事件【{}】播放电影:data={}",SOCKET_MOVIE,data);
            //观看前用户检测
            boolean b = socketMovieService.doSockeMovie(client, data.getUserId(),data.getMovieId(),SOCKET_RESULT);
            if(b){
                Long movieId = data.getMovieId();
                client.joinRoom(SOCKET_MOVIE + movieId);//分配电影房间
                //查询对应房间连接数
                Integer roomClientNum = getRoomClientNum(client, SOCKET_MOVIE + movieId);
                //更新对应电影在线观看人数
                socketMovieService.updateMovieWatchNum(movieId,roomClientNum,0);
                client.sendEvent(SOCKET_RESULT, SocketResultVo.success(SocketResultTypeEnum.RESULT_WATCH_MOVIE.getCode()));
            }
        });
        //监听退出播放电影
        socketIOServer.addEventListener(SOCKET_MOVIE_DISCONNECT, SocketMovieReq.class, (client, data, ackSender) -> {
            log.info("事件【{}】退出播放电影:ThreadName:{}" ,SOCKET_MOVIE_DISCONNECT, Thread.currentThread().getName());
            log.info("事件【{}】退出播放电影:data={}",SOCKET_MOVIE_DISCONNECT,data);
            Long movieId = data.getMovieId();
            client.leaveRoom(SOCKET_MOVIE + movieId);//退出电影房间
            //查询对应房间连接数
            Integer roomClientNum = getRoomClientNum(client, SOCKET_MOVIE + movieId);
            //更新对应电影在线观看人数
            socketMovieService.updateMovieWatchNum(movieId,roomClientNum,1);
            client.sendEvent(SOCKET_RESULT, SocketResultVo.success(SocketResultTypeEnum.RESULT_QUIT_MOVIE.getCode()));
        });
        //监听后台禁用用户 强制下线
        socketIOServer.addEventListener(STOP_USER, PushMessage.class, (client, data, ackSender) -> {
            log.info("事件【{}】监听后台禁用用户:ThreadName:{}" ,STOP_USER, Thread.currentThread().getName());
            log.info("事件【{}】监听后台禁用用户:data={}",STOP_USER,data);
            pushMessageToUser(data,STOP_USER);//单发
        });

        //加载直播监听
        SocketLiveCurrentDataVo socketLiveCurrentDataVo = new SocketLiveCurrentDataVo();
        new LiveSocketService(socketIOServer,clientLiveMap,socketLiveService,socketLiveCurrentDataVo,redisService,clientMap);
        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 推送指定用户
     * @param pushMessage
     */
    @Override
    public void pushMessageToUser(PushMessage pushMessage,String type) {
        log.info("pushMessageToUser单发:ThreadName:" + Thread.currentThread().getName());
        log.info("pushMessageToUser单发:data={}",pushMessage);
        String loginUserId = pushMessage.getLoginUserId().toString();
        if (StrKit.isNotEmpty(loginUserId)) {
            SocketIOClient client = clientMap.get(loginUserId);
            if (client != null)
                client.sendEvent(type, pushMessage);
        }
    }

    /**
     * 推送所有用户
     * @param pushMessage
     */
    public void pushAllUsers(PushMessage pushMessage){
        log.info("pushAllUsers群发:ThreadName:" + Thread.currentThread().getName());
        log.info("pushAllUsers群发:data={}",pushMessage);
        this.socketIOServer.getBroadcastOperations().sendEvent(PUSH_ALL, pushMessage);
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("userId");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    private Integer getRoomClientNum(SocketIOClient client,String roomId){
        log.info("查询房间{}的人数",roomId);
        BroadcastOperations loginRoom = this.socketIOServer.getNamespace("").getRoomOperations(roomId);
        Collection<SocketIOClient> clients = loginRoom.getClients();
        log.info("房间{}的人数是【{}】",roomId,clients.size());
        return clients.size();
    }

    public void listenDis(SocketIOClient client){
        //让用户退出登录
        String loginUserIdByClient = this.getLoginUserIdByClient(client);
        clientMap.remove(loginUserIdByClient);
        if(StrKit.isNotEmpty(loginUserIdByClient)){
            UserRabbitMessage userRabbitMessage = new UserRabbitMessage();
            userRabbitMessage.setType(MqUserServiceTypeConstens.USER_LOGOUT);
            userRabbitMessage.setUserId(Long.valueOf(loginUserIdByClient));
            mqSender.sendUserLogoutMessage(userRabbitMessage);
        }
        //查询是否有直播
        String liveStream = this.getLiveStreamByClient(client);
        if(StrKit.isNotEmpty(liveStream)){
            clientLiveMap.remove(liveStream);
        }

        Set<String> allRooms = client.getAllRooms();
        for (String allRoom : allRooms) {
            boolean contains = allRoom.contains(SOCKET_MOVIE);
            if(contains){//断开连接的用户存在于电影房
                String[] split = allRoom.split(":");
                String movieIdStr = split[1];
                Integer roomClientNum = getRoomClientNum(client, allRoom);
                socketMovieService.updateMovieWatchNum(Long.valueOf(movieIdStr),roomClientNum,1);
            }
        }
    }

    public String getLoginUserIdByClient(SocketIOClient client){
        Set<String> keys = clientMap.keySet();
        for (String key : keys) {
            SocketIOClient socketIOClient = clientMap.get(key);
            if(socketIOClient == client){
                return key;
            }
        }
        return "";
    }
    public String getLiveStreamByClient(SocketIOClient client){
        Set<String> keys = clientLiveMap.keySet();
        for (String key : keys) {
            SocketIOClient socketIOClient = clientLiveMap.get(key);
            if(socketIOClient == client){
                return key;
            }
        }
        return "";
    }

}
