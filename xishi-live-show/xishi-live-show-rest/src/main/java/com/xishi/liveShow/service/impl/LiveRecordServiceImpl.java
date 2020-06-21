package com.xishi.liveShow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.StrKit;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.liveShow.feign.UserService;
import com.xishi.liveShow.entity.req.LiveReq;
import com.xishi.liveShow.util.DateUtils;
import com.xishi.liveShow.util.LiveUrlUtil;
import com.xishi.liveShow.entity.vo.LiveDetailVo;
import com.xishi.liveShow.entity.vo.LiveShowDetailVo;
import com.xishi.liveShow.entity.vo.LiveShowVo;
import com.xishi.liveShow.entity.vo.LiveVo;
import com.xishi.liveShow.model.pojo.Live;
import com.xishi.liveShow.model.pojo.LiveRecord;
import com.xishi.liveShow.dao.mapper.LiveRecordMapper;
import com.xishi.liveShow.service.ILiveRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.liveShow.service.ILiveService;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.user.entity.req.UserSearchQuery;
import com.xishi.user.entity.vo.UserInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
@Service
public class LiveRecordServiceImpl extends ServiceImpl<LiveRecordMapper, LiveRecord> implements ILiveRecordService {

    @Autowired
    private ILiveService liveService;
    @Autowired
    private UserService userService;
    @Autowired
    private LiveUrlUtil liveUrlUtil;

    @Override
    public Resp<LiveVo> getLastLiveRecord(Long userId) {
        Resp<Live> resp  = liveService.checkLive(userId);
        if(!resp.isSuccess()){
            return Resp.error(resp.getMsg());
        }
        Live data = resp.getData();
        LiveRecord byId = this.getById(resp.getData().getNewLiveRecord());
        if(byId == null){
            return Resp.successData(new LiveVo());
        }
        LiveVo liveVo = TransUtil.transEntity(byId, LiveVo.class);
        liveVo.setLiveState(data.getLiveState());
        return Resp.successData(liveVo);
    }

    @Override
    public Resp<LiveShowVo> genLiveShow(LiveReq data) {
        Resp<Live> resp  = liveService.checkLive(data.getUserId());
        if(!resp.isSuccess()){
            return Resp.error(resp.getMsg());
        }
        Live live = resp.getData();
        Integer liveMode = data.getLiveMode();

        //获取用户信息
        Resp<UserInfoVo> userInfoVoResp = userService.queryUserInfo(new Req<>(data.getUserId()));
        UserInfoVo userInfoVo = userInfoVoResp.getData();
        if(userInfoVo == null){
            return Resp.error("用户不存在");
        }
        if(userInfoVo.getState() != 0){
            return Resp.error("用户已被冻结");
        }
        LiveRecord liveRecord = TransUtil.transEntity(data, LiveRecord.class);
        if(liveMode == 1){
            if(StrKit.isEmpty(data.getLivePwd()))return Resp.error("参数错误");
        }else if(liveMode > 1){
            BigDecimal livePrice = data.getLivePrice();
            if(livePrice.compareTo(BigDecimal.ZERO) < 0)return Resp.error("参数错误");
        }
        liveRecord.setShowerName(userInfoVo.getName());
        liveRecord.setLiveId(live.getId());
        liveRecord.setUserId(data.getUserId());
        liveRecord.setState(0);
        this.save(liveRecord);
        //更新live相关信息
        live.setNewLiveRecord(liveRecord.getId());
        live.setLiveState(1);
        live.setLiveStartTime(new Date());
        live.setUpdateTime(new Date());
        //生成推流地址
        String pushUrl = liveUrlUtil.genPushUrl(live.getStreamName());
        live.setPushUrl(pushUrl);
        //生成播流地址
        String pullUrl = liveUrlUtil.genPullUrl(live.getStreamName(),"RTMP");
        live.setPullUrl(pullUrl);
        liveService.updateById(live);

        LiveShowVo liveVo = TransUtil.transEntity(liveRecord, LiveShowVo.class);
        liveVo.setPushUrl(pushUrl);
        liveVo.setStreamName(live.getStreamName());
        return Resp.successData(liveVo);
    }

    @Override
    public Resp<List<LiveShowDetailVo>> liveShowDetail(UserSearchQuery query) {
        PageHelper.startPage(query.getPage(),query.getRows());
        List<LiveRecord> liveRecords = this.list(new QueryWrapper<LiveRecord>().eq("user_id", query.getUserId()).eq("state",1).orderByDesc("id"));
        System.out.println(liveRecords);
        List<LiveShowDetailVo> liveShowDetailVos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(liveRecords)){
            liveShowDetailVos = TransUtil.transList(liveRecords, LiveShowDetailVo.class);
            for (LiveShowDetailVo liveShowDetailVo : liveShowDetailVos) {
                liveShowDetailVo.setShowTime(DateUtils.getDurTime(liveShowDetailVo.getCreateTime(),liveShowDetailVo.getEndTime()));
            }
        }

        return Resp.successData(liveShowDetailVos);
    }

    @Override
    public Resp<Void> changeLivePlayNum(MovieQuery data) {
        Live byId = liveService.getById(data.getId());
        data.setId(byId.getNewLiveRecord());
        this.baseMapper.changeLivePlayNum(data);
        return Resp.success();
    }

    @Override
    public Resp<Void> changeLiveWatchNum(MovieQuery data) {
        Live byId = liveService.getById(data.getId());
        data.setId(byId.getNewLiveRecord());
        Integer count = data.getCount();
        if(count < 0){
            data.setCount(0);
        }
        this.baseMapper.changeLiveWatchNum(data);
        return Resp.success();
    }

    @Override
    public Resp<LiveDetailVo> getLiveInfo(Long liveId) {
        Live byId = liveService.getById(liveId);
        LiveDetailVo liveDetailVo = TransUtil.transEntity(byId, LiveDetailVo.class);
        return Resp.successData(liveDetailVo);
    }

}
