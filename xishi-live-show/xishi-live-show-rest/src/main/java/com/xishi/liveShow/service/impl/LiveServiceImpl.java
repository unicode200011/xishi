package com.xishi.liveShow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.liveShow.feign.MovieService;
import com.xishi.liveShow.feign.UserService;
import com.xishi.liveShow.mq.MqLiveShowSender;
import com.xishi.liveShow.entity.req.LiveListQuery;
import com.xishi.liveShow.entity.req.LiveRoomReq;
import com.xishi.liveShow.entity.req.SendMessageReq;
import com.xishi.liveShow.entity.req.SortListQuery;
import com.xishi.liveShow.util.DateUtils;
import com.xishi.liveShow.util.LiveUrlUtil;
import com.xishi.liveShow.entity.vo.*;
import com.xishi.liveShow.model.pojo.Live;
import com.xishi.liveShow.dao.mapper.LiveMapper;
import com.xishi.liveShow.model.pojo.LiveRecord;
import com.xishi.liveShow.service.ILiveRecordService;
import com.xishi.liveShow.service.ILiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.entity.req.UserAttentionQuery;
import com.xishi.user.entity.req.UserSearchQuery;
import com.xishi.user.entity.vo.UserInfoVo;
import com.xishi.user.entity.vo.UserWalletVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisAccessor;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
@Service
@Slf4j
public class LiveServiceImpl extends ServiceImpl<LiveMapper, Live> implements ILiveService {

    @Autowired
    LiveMapper liveMapper;
    @Autowired
    private ILiveRecordService liveRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    LiveUrlUtil liveUrlUtil;
    @Autowired
    private MqLiveShowSender mqLiveShowSender;
    @Autowired
    RedisService redisService;
    @Override
    public Resp<Live> checkLive(Long userId) {
        Live live = this.getOne(new QueryWrapper<Live>().eq("user_id", userId));
        if(live == null){
            return Resp.error("请先申请成为主播");
        }
        if(live.getState() == 1){
            return Resp.error("你已被禁播");
        }
        return Resp.successData(live);
    }

    @Override
    public Resp<List<LiveListVo>> getLiveList(LiveListQuery data) {
        log.info("date={}",data);
        PageHelper.startPage(data.getPage(),data.getRows());
        List<LiveListVo> lives = liveMapper.getLiveList(data);
        log.info("lives={}",lives);
        return Resp.successData(lives);
    }

    @Override
    public Resp<List<LiveListVo>> getSortList(SortListQuery data) {
        data.setType(0);
        List<SortUserVo> sortUserVos = liveMapper.getSortUserList(data);

        List<Long> collect = sortUserVos.stream().map(SortUserVo::getUserId).filter(getUserId -> getUserId != data.getUserId()).collect(Collectors.toList());
        List<LiveListVo> LiveListVos = liveMapper.getSortLiveList(collect);
        return Resp.successData(LiveListVos);
    }

    @Override
    public Resp<List<SortUserVo>> getSortUserList(SortListQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        System.out.println("页数:" + data.getPage());
        System.out.println("每页数据:" + data.getRows());
        List<SortUserVo> sortUserVos = liveMapper.getSortUserList(data);
        return Resp.successData(sortUserVos);
    }

    @Override
    public Resp<LiveShowPullVo> viewLiveShow(LiveRoomReq data) {
        log.info("用户请求进入直播间：data【{}】", JSON.toJSONString(data));
        //TODO 判断直播信息
        Long liveId = data.getLiveId();
        Live dbLive = this.getById(liveId);
        if(dbLive ==null){
            return Resp.error("参数错误");
        }
        Integer liveState = dbLive.getLiveState();

        LiveRecord liveRecord = liveRecordService.getById(dbLive.getNewLiveRecord());
        if(liveRecord == null){
            liveRecord = new LiveRecord();
            liveRecord.setLiveId(liveId);
        }
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(new Req<>(dbLive.getUserId()));
        UserInfoVo showerInfo = userInfoVoResp.getData();
        //TODO 判断用户相关信息
        String key = RedisConstants.LIVE_USER_GET_OUT+dbLive.getNewLiveRecord()+data.getUserId();
        boolean exists = redisService.exists(key);
        if(exists && liveState == 1){
            return Resp.error("你已被主播踢出房间");
        }
        if(liveRecord != null && liveState ==1){
            Integer liveMode = liveRecord.getLiveMode();
            switch (liveMode){
                case 1:{ //密码模式
                    String livePwd = liveRecord.getLivePwd();
                    if(!livePwd.equals(data.getLivePwd())){
                        return Resp.error("密码错误");
                    }

                    redisService.set(RedisConstants.LIVE_USER_PWD + liveRecord.getId() + data.getUserId(), livePwd);

                    break;
                }
                case 2:{//常规计费
                    Resp<UserWalletVo> userWalletVoResp = userService.checkUserWallet(new Req<>(data.getUserId()));
                    UserWalletVo userWalletVo = userWalletVoResp.getData();
                    BigDecimal gbMoeny = userWalletVo.getGbMoeny();
                    log.info("用户请求进入直播间：用户账户余额【{}】价格【{}】", gbMoeny,liveRecord.getLivePrice());
                    if(gbMoeny.compareTo(liveRecord.getLivePrice()) < 0) {
                        return  new Resp<>(503,"账户余额不足");
                    }
                    boolean isPayed = redisService.exists(RedisConstants.LIVE_USER_PAYED + liveRecord.getId() + data.getUserId());
                    if(!isPayed){
                        //TODO 扣费
                        LivePayRabbitMessage livePayRabbitMessage = new LivePayRabbitMessage();
                        livePayRabbitMessage.setLiveId(liveId);
                        livePayRabbitMessage.setLiveRecordId(dbLive.getNewLiveRecord());
                        livePayRabbitMessage.setLiveUserId(dbLive.getUserId());
                        livePayRabbitMessage.setPrice(liveRecord.getLivePrice());
                        livePayRabbitMessage.setUserId(data.getUserId());
                        livePayRabbitMessage.setStreamName(dbLive.getStreamName());
                        mqLiveShowSender.sendLivePayMessage(livePayRabbitMessage);
                        //缓存本场支付记录
                        redisService.set(RedisConstants.LIVE_USER_PAYED+liveRecord.getId()+data.getUserId(),1);
                    }
                }
                case 3: {//计时收费
                    Resp<UserWalletVo> userWalletVoResp = userService.checkUserWallet(new Req<>(data.getUserId()));
                    UserWalletVo userWalletVo = userWalletVoResp.getData();
                    BigDecimal gbMoeny = userWalletVo.getGbMoeny();
                    if(gbMoeny.compareTo(liveRecord.getLivePrice()) < 0) {
                        return  new Resp<>(503,"账户余额不足");
                    }
                    break;
                }
            }
        }

        //封装返回结果
        String streamName = dbLive.getStreamName();
        String pullUrl = liveUrlUtil.genPullUrl(streamName, data.getPlayType());
        log.info("用户请求进入直播间：播流地址【{}】",pullUrl);
        LiveShowPullVo liveShowPullVo = new LiveShowPullVo();
        if(liveRecord != null){
            liveShowPullVo = TransUtil.transEntity(liveRecord, LiveShowPullVo.class);
        }
        liveShowPullVo.setPullUrl(pullUrl);
        liveShowPullVo.setLiveId(liveId);
        liveShowPullVo.setStreamName(dbLive.getStreamName());
        liveShowPullVo.setLiveState(dbLive.getLiveState());
        liveShowPullVo.setAvatar(showerInfo.getAvatar());
        liveShowPullVo.setUserName(showerInfo.getName());
        UserAttentionQuery query = new UserAttentionQuery();
        query.setUserId(data.getUserId());
        query.setLinkUid(dbLive.getUserId());
        Resp<Integer> integerResp = userService.checkAttention(new Req<>(query));
        Integer attention = integerResp.getData();
        liveShowPullVo.setAttention(attention);
        if(dbLive.getLiveState() == 1){
            liveShowPullVo.setCreateDateTime(dbLive.getLiveStartTime().getTime());
        }
        return Resp.successData(liveShowPullVo);
    }

    @Override
    public void discBack(String stream_id) {
        //清除直播间相关缓存
        Live dbLive = this.getOne(new QueryWrapper<Live>().eq("stream_name", stream_id));
        if(dbLive != null){
            String userListKey = RedisConstants.LIVE_USER_LIST+dbLive.getNewLiveRecord();
            redisService.remove(userListKey);
        }
        this.changeLiveState(stream_id,0,1);
    }

    @Override
    public void pushBack(String stream_id) {
        this.changeLiveState(stream_id,1,0);
    }

    @Override
    public Resp<SearchVo> searchLive(UserSearchQuery query) {
        Resp<List<MovieVo>> movies = movieService.searchMovie(new Req<UserSearchQuery>(query));
        Resp<List<UserInfoVo>> users = userService.searchUser(new Req<UserSearchQuery>(query));
        PageHelper.startPage(query.getPage(),query.getRows());
        List<LiveListVo> LiveListVos = liveMapper.searchLive(query.getKeyword());
        SearchVo searchVo = new SearchVo();
        searchVo.setLiveVos(LiveListVos);
        searchVo.setMovieVos(movies.getData());
        searchVo.setUserInfoVos(users.getData());
        return Resp.successData(searchVo);
    }

    @Override
    public Resp<Void> changeMode(LiveRoomReq data) {
        Live byId = this.getById(data.getLiveId());
        Long newLiveRecord = byId.getNewLiveRecord();
        LiveRecord liveRecord = liveRecordService.getById(newLiveRecord);
        liveRecord.setLiveMode(data.getLiveMode());
        if(data.getLiveMode() == 1){
            liveRecord.setLivePwd(data.getLivePwd());
        }else if(data.getLiveMode() > 1){
            BigDecimal livePrice = data.getLivePrice();
            if(livePrice.compareTo(BigDecimal.ZERO) <= 0){
                return Resp.error("价格必须大于0");
            }
            liveRecord.setLivePrice(data.getLivePrice());
        }else {
            liveRecord.setLivePrice(BigDecimal.ZERO);
        }
        liveRecordService.updateById(liveRecord);
        return Resp.success();
    }

    @Override
    public Resp<LiveEndDataReq> endLiveShowData(LiveRoomReq data) {
        Live byId = this.getById(data.getLiveId());
        Long newLiveRecord = byId.getNewLiveRecord();
        LiveRecord liveRecord = liveRecordService.getById(newLiveRecord);
        LiveEndDataReq liveEndDataReq = new LiveEndDataReq();
        liveEndDataReq.setLiveWatchNum(liveRecord.getLiveWatchTotal());
        liveEndDataReq.setUserId(data.getUserId());
        long time = liveRecord.getCreateTime().getTime();
        long now = new Date().getTime();
        long l = now - time;
        liveEndDataReq.setLiveTime(l / 1000 / 60);
        liveEndDataReq.setLiveId(data.getLiveId());
        LiveEndDataReq detail  = baseMapper.getLiveData(newLiveRecord);
        liveEndDataReq.setAmount(detail.getAmount());
        liveEndDataReq.setGiveUserNum(detail.getGiveUserNum());
        return Resp.successData(liveEndDataReq);
    }

    @Override
    public Resp<Void> stopSendMessage(SendMessageReq data) {
        Long liveId = data.getLiveId();
        Live live = this.getById(liveId);
        Integer type = data.getType();
        if(type == 0){
            redisService.set(RedisConstants.USER_STOP_SEND_MESSAGE+live.getNewLiveRecord(),1);
        }else {
            redisService.remove(RedisConstants.USER_STOP_SEND_MESSAGE+live.getNewLiveRecord());
        }
        return Resp.success();
    }

    @Override
    public Resp<LiveCheckPayVo> checkLivePay(LiveRoomReq data) {
        Live dbLive = this.getById(data.getLiveId());
        if(dbLive == null){
            return Resp.successData(new LiveCheckPayVo());
        }
        LiveRecord byId = liveRecordService.getById(dbLive.getNewLiveRecord());
        if(byId == null){
            LiveCheckPayVo liveCheckPayVo = new LiveCheckPayVo();
            liveCheckPayVo.setLiveId(data.getLiveId());
            return Resp.successData(liveCheckPayVo);
        }
        boolean isPayed = redisService.exists(RedisConstants.LIVE_USER_PAYED + dbLive.getNewLiveRecord() + data.getUserId());
        boolean isPwd = redisService.exists(RedisConstants.LIVE_USER_PWD + dbLive.getNewLiveRecord() + data.getUserId());
        LiveCheckPayVo liveCheckPayVo = TransUtil.transEntity(byId, LiveCheckPayVo.class);
        liveCheckPayVo.setIsPayed(isPayed?1:0);
        liveCheckPayVo.setIsPwd(isPwd ? 1 : 0);
        liveCheckPayVo.setLiveState(dbLive.getLiveState());
        liveCheckPayVo.setLiveId(dbLive.getId());
        return Resp.successData(liveCheckPayVo);
    }

    public void changeLiveState(String stream_id,Integer liveState,Integer recordState){
        Live stream_name = this.getOne(new QueryWrapper<Live>().eq("stream_name", stream_id));
        if(stream_name != null){
            stream_name.setLiveState(liveState);
            if(liveState == 1){ //开始直播
                stream_name.setLiveStartTime(new Date());
            }
        }
        this.updateById(stream_name);

        Long newLiveRecord = stream_name.getNewLiveRecord();
        LiveRecord byId = liveRecordService.getById(newLiveRecord);
        byId.setState(recordState);
        if(recordState == 1){ //直播结束
            byId.setEndTime(new Date());
            byId.setShowTime(DateUtils.getDurTime(byId.getCreateTime(),new Date()));
            //更新直播总数据
            Long liveId = byId.getLiveId();
            Live live = this.getById(liveId);
            Long totalLiveTime = live.getTotalLiveTime();
            Long totalLiveWatch = live.getTotalLiveWatch();
            live.setTotalLiveWatch(totalLiveWatch+byId.getLiveWatchTotal());
            live.setTotalLiveTime(totalLiveTime+byId.getShowTime());
            this.updateById(live);
        }
        liveRecordService.updateById(byId);
    }

}
