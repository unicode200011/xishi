package com.xishi.socket.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.MapUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.socket.entity.req.SocketLivePayReq;
import com.xishi.socket.entity.req.SocketLiveReq;
import com.xishi.socket.entity.req.SocketLiveSendGiftReq;
import com.xishi.socket.entity.req.SocketLiveSendMessageReq;
import com.xishi.socket.entity.vo.LiveDetailVo;
import com.xishi.socket.entity.vo.SocketLiveCurrentDataVo;
import com.xishi.socket.entity.vo.SocketUserVo;
import com.xishi.socket.feign.LiveService;
import com.xishi.socket.feign.UserService;
import com.xishi.socket.mq.MqSender;
import com.xishi.socket.service.SocketLiveService;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.entity.mqMessage.LiveRabbitMessage;
import com.xishi.user.entity.req.WatchRecordReq;
import com.xishi.user.entity.vo.LiveGiftRankingVo;
import com.xishi.user.entity.vo.UserInfoVo;
import com.xishi.user.entity.vo.UserWalletVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class SocketLiveServiceImpl implements SocketLiveService {

    @Autowired
    LiveService liveService;
    @Autowired
    RedisService redisService;
    @Autowired
    MqSender mqSender;
    @Autowired
    UserService userService;
    /**
     * 跟新直播观看人数
     * @param
     * @param roomClientNum
     * @param io
     */
    @Override
    public SocketLiveCurrentDataVo updateLiveWatchNum(SocketLiveReq data, Integer roomClientNum, int io) {

        //获取用户等级颜色
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(new Req<>(data.getUserId()));
        UserInfoVo userInfoVo = userInfoVoResp.getData();
        data.setRgb(userInfoVo.getUserLevelColor());

        MovieQuery movieQuery = new MovieQuery();
        movieQuery.setId(data.getLiveId());
        movieQuery.setIo(io);
        movieQuery.setCount(roomClientNum);
        Req<MovieQuery> userReq = new Req<MovieQuery>(movieQuery);
        log.info("更新观看数量LiveQuery==={}",movieQuery);
        liveService.changeLiveWatchNum(userReq);//更新当前观看人数
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        log.info("获取直播相关信息==={}",liveDetailVo);
//        String userListKey = RedisConstants.LIVE_USER_LIST+liveDetailVo.getNewLiveRecord();
//        SocketUserVo socketUserVo = new SocketUserVo();
//        socketUserVo.setAvatar(data.getAvatar());
//        socketUserVo.setUserId(data.getUserId());
//        //将用户信息加入实时更新数据中用户列表
//        Object cacheList = redisService.get(userListKey);
//        Set<SocketUserVo> socketUserVos = new HashSet<>();
//        if(cacheList != null){
//            socketUserVos = (Set<SocketUserVo>)cacheList;
//        }
        WatchRecordReq watchRecordReq = new WatchRecordReq();
        watchRecordReq.setLiveRecordId(liveDetailVo.getNewLiveRecord());
        watchRecordReq.setUserId(data.getUserId());
        if(io == 0){
            //添加观看记录
            userService.addWatchRecord(new Req<>(watchRecordReq));
            //判断用户是否之前加入过 第一次加入直播间 则 加入缓存 更新播放总量
            String key = RedisConstants.LIVE_USER_ADD+liveDetailVo.getNewLiveRecord()+data.getUserId();
            if (!redisService.exists(key)) {
                liveService.changeLivePlayNum(userReq);//更新播放总量
                redisService.set(key,data.getLiveId(),6000L);
            }
//            socketUserVos.add(socketUserVo);
        }
        else if(io == 1) { //退出直播间
            //更新观看记录
            watchRecordReq.setState(1);
            watchRecordReq.setEndTime(new Date());
            userService.updateWatchRecord(new Req<>(watchRecordReq));
//            Iterator<SocketUserVo> it = socketUserVos.iterator();
//            while(it.hasNext()){
//                SocketUserVo checkWork = it.next();
//                if(checkWork.getUserId() == data.getUserId()){
//                    it.remove();
//                }
//            }
        }
//        redisService.set(userListKey,socketUserVos,6000L);

        //更新对应直播间 在线人数
        String userNumKey = RedisConstants.LIVE_USER_NUM+liveDetailVo.getNewLiveRecord();
        SocketLiveCurrentDataVo socketLiveCurrentDataVo = new SocketLiveCurrentDataVo();
        if(redisService.exists(userNumKey)){
            Object o = redisService.get(userNumKey);
            socketLiveCurrentDataVo = (SocketLiveCurrentDataVo)o;
        }
        if(liveDetailVo.getLiveState() == 0){
            roomClientNum = roomClientNum+1;
        }
        socketLiveCurrentDataVo.setUserNum(roomClientNum<0?0:roomClientNum);

        //获取直播间顶部用户排行榜前三
        Resp<List<LiveGiftRankingVo>> liveGiftRanking = userService.getLiveGiftRanking(new Req<>(liveDetailVo.getNewLiveRecord()));
        List<LiveGiftRankingVo> rankingVos = liveGiftRanking.getData();

        log.info("获取直播间顶部用户排行榜前三[{}]", JSON.toJSONString(rankingVos));
        Set<SocketUserVo> socketUserVos = new LinkedHashSet<>();
        for (LiveGiftRankingVo rankingVo : rankingVos) {
            SocketUserVo socketUserVo = new SocketUserVo();
            socketUserVo.setAvatar(rankingVo.getAvatar());
            socketUserVo.setUserId(rankingVo.getUserId());
            socketUserVos.add(socketUserVo);
        }
        socketLiveCurrentDataVo.setUserVos(socketUserVos);
        redisService.set(userNumKey,socketLiveCurrentDataVo);
        return socketLiveCurrentDataVo;
    }

    @Override
    public void doGetOut(SocketLivePayReq data, String roomId) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        String key = RedisConstants.LIVE_USER_GET_OUT+liveDetailVo.getNewLiveRecord()+data.getUserId();
        redisService.set(key,roomId,24*60*60L);
    }

    @Override
    public void doNoSend(SocketLivePayReq data, String roomId) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        String key = RedisConstants.LIVE_USER_NO_SNED+liveDetailVo.getNewLiveRecord()+data.getUserId();
        redisService.set(key,roomId,24*60*60L);
    }

    @Override
    public boolean checkNoSend(SocketLiveSendMessageReq data) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        String key = RedisConstants.LIVE_USER_NO_SNED+liveDetailVo.getNewLiveRecord()+data.getUserId();

        return redisService.exists(key) ;
    }

    public static void main(String[] args) {
        BigDecimal gbMoeny = BigDecimal.valueOf(100);
        BigDecimal times = gbMoeny.divide(BigDecimal.valueOf(20), 0, BigDecimal.ROUND_DOWN);
        System.out.println(times);
    }

    @Override
    public void doTimeCount(SocketLivePayReq data, String roomId, SocketIOClient client) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        if(data.getUserId() == liveDetailVo.getUserId()){
            return;
        }
        String timeCountKey = RedisConstants.LIVE_USER_TIME_COUNT+liveDetailVo.getNewLiveRecord()+data.getUserId();
        Integer timeCount = 1;
        Object o = redisService.get(timeCountKey);
        if(o != null){
            timeCount = (int)o;
            timeCount = timeCount + 1;
        }
        log.info("用户【{}】已观看分钟数【{}】直播roomId是【{}】",data.getUserId(),timeCount,roomId);
        redisService.set(timeCountKey,timeCount,24*60*60L);
        //计算剩余金额可观看分钟数
        Resp<UserWalletVo> userWalletVoResp = userService.checkUserWallet(new Req<>(data.getUserId()));
        UserWalletVo userWalletVo = userWalletVoResp.getData();
        BigDecimal gbMoeny = userWalletVo.getGbMoeny();
        log.info("西施币:{}", data.getLivePrice());
        log.info("是否:{}", data.getLivePrice().compareTo(BigDecimal.ZERO) > 0);
        if(data.getLivePrice().compareTo(BigDecimal.ZERO) > 0){
            log.info("用户钱包【{}】直播价格是【{}】",gbMoeny,data.getLivePrice());
            BigDecimal times = gbMoeny.divide(data.getLivePrice(), 0, BigDecimal.ROUND_DOWN);
            log.info("用户【{}】可观看直播分钟数是【{}】",data.getUserId(),times);
            if(times.compareTo(BigDecimal.valueOf(5)) <= 0){
                log.info("用户【{}】可观看直播分钟数不足五分钟，剩余【{}】分钟",data.getUserId(),times);
                client.sendEvent("live_surplus_time", MapUtil.build().put("times",times).over());
            }
            if (times.compareTo(BigDecimal.valueOf(0)) >= 0){
                //每分钟扣费一次
                LivePayRabbitMessage livePayRabbitMessage = new LivePayRabbitMessage();
                livePayRabbitMessage.setLiveMode(1);
                livePayRabbitMessage.setStreamName(liveDetailVo.getStreamName());
                livePayRabbitMessage.setUserId(data.getUserId());
                livePayRabbitMessage.setPrice(data.getLivePrice());
                livePayRabbitMessage.setLiveUserId(liveDetailVo.getUserId());
                livePayRabbitMessage.setLiveTime(1);
                livePayRabbitMessage.setLiveRecordId(liveDetailVo.getNewLiveRecord());
                livePayRabbitMessage.setLiveId(liveDetailVo.getId());
                mqSender.sendLivePayMessage(livePayRabbitMessage);
            }
        }
    }

    @Override
    public void doPayTimeCount(SocketLivePayReq data, String roomId) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();

        //更新用户钱包记录  计时收费模式
        LivePayRabbitMessage livePayRabbitMessage = new LivePayRabbitMessage();
        livePayRabbitMessage.setLiveMode(2);
        livePayRabbitMessage.setStreamName(liveDetailVo.getStreamName());
        livePayRabbitMessage.setUserId(data.getUserId());
        livePayRabbitMessage.setPrice(data.getLivePrice());
        livePayRabbitMessage.setLiveUserId(liveDetailVo.getUserId());
        livePayRabbitMessage.setLiveTime(1);
        livePayRabbitMessage.setLiveRecordId(liveDetailVo.getNewLiveRecord());
        livePayRabbitMessage.setLiveId(liveDetailVo.getId());
        mqSender.sendLivePayRecordMessage(livePayRabbitMessage);
    }

    @Override
    public void doEndLive(SocketLiveReq data) {
        LiveRabbitMessage liveRabbitMessage = new LiveRabbitMessage();
        liveRabbitMessage.setLiveId(data.getLiveId());
        liveRabbitMessage.setStreamName(data.getStreamName());
        mqSender.sendEndLiveMessage(liveRabbitMessage);
    }

    @Override
    public boolean checkAllNoSend(SocketLiveSendMessageReq data) {
        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();
        if(liveDetailVo.getUserId() == data.getUserId()){
            return false;
        }
        //全员禁言
        boolean exists = redisService.exists(RedisConstants.USER_STOP_SEND_MESSAGE + liveDetailVo.getNewLiveRecord());
        if(exists){
            return true;
        }
        return false;
    }

    @Override
    public SocketLiveCurrentDataVo sendGift(SocketLiveSendGiftReq data) {

        //获取直播相关信息
        Resp<LiveDetailVo> liveInfo = liveService.getLiveInfo(new Req<>(data.getLiveId()));
        LiveDetailVo liveDetailVo = liveInfo.getData();

        String userNumKey = RedisConstants.LIVE_USER_NUM+liveDetailVo.getNewLiveRecord();
        SocketLiveCurrentDataVo socketLiveCurrentDataVo = new SocketLiveCurrentDataVo();
        if(redisService.exists(userNumKey)){
            Object o = redisService.get(userNumKey);
            socketLiveCurrentDataVo = (SocketLiveCurrentDataVo)o;
        }

        BigDecimal amount = socketLiveCurrentDataVo.getAmount();
        BigDecimal multiply = data.getPrice().multiply(BigDecimal.valueOf(data.getGiftNum()));
        BigDecimal add = amount.add(multiply);
        socketLiveCurrentDataVo.setAmount(add);

        //获取直播间顶部用户排行榜前三
        Resp<List<LiveGiftRankingVo>> liveGiftRanking = userService.getLiveGiftRanking(new Req<>(liveDetailVo.getNewLiveRecord()));
        List<LiveGiftRankingVo> rankingVos = liveGiftRanking.getData();

        log.info("获取直播间顶部用户排行榜前三2[{}]", JSON.toJSONString(rankingVos));
        Set<SocketUserVo> socketUserVos = new LinkedHashSet<>();
        for (LiveGiftRankingVo rankingVo : rankingVos) {
            SocketUserVo socketUserVo = new SocketUserVo();
            socketUserVo.setAvatar(rankingVo.getAvatar());
            socketUserVo.setUserId(rankingVo.getUserId());
            socketUserVos.add(socketUserVo);
        }

        log.info("socketUserVos[{}]", socketUserVos);
        socketLiveCurrentDataVo.setUserVos(socketUserVos);

        redisService.set(userNumKey,socketLiveCurrentDataVo);
        return socketLiveCurrentDataVo;
    }

    @Override
    public void checkUserInfo(SocketLiveSendMessageReq data) {

        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(new Req<>(data.getUserId()));
        //屏蔽关键字
        Resp<String> word = userService.replaceWord(new Req<String>(data.getMessage()));
        data.setMessage(word.getData());
        UserInfoVo userInfoVo = userInfoVoResp.getData();
        data.setRgb(userInfoVo.getUserLevelColor());
        data.setUserLevel(userInfoVo.getUserLevel());
    }

    @Override
    public boolean checkUserLevel(SocketLiveSendMessageReq data, Integer roomClientNum) {
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(new Req<>(data.getUserId()));
        UserInfoVo userInfoVo = userInfoVoResp.getData();
        Integer userLevel = userInfoVo.getUserLevel();

        Resp<String> stringResp = userService.checkLevel(new Req<>());
        Integer levelStr = Integer.valueOf(stringResp.getData());

        if(roomClientNum < 1001 ){ //二级以上用户才能发言
            if(userLevel >=levelStr){
                return  true;
            }
        }else if(roomClientNum < 5001){//五级以上用户才能发言
            if(userLevel >5){
                return  true;
            }
        }else if(roomClientNum > 5000){//十级用户才能发言
            if(userLevel >9){
                return  true;
            }
        }
        return false;
    }
}
