package com.xishi.liveShow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.xishi.liveShow.dao.mapper.GiftMapper;
import com.xishi.liveShow.feign.UserService;
import com.xishi.liveShow.mq.MqLiveShowSender;
import com.xishi.liveShow.entity.req.GiftListQuery;
import com.xishi.liveShow.entity.req.LiveGiftListQuery;
import com.xishi.liveShow.entity.req.SendGiftReq;
import com.xishi.liveShow.service.IGiftService;
import com.xishi.liveShow.service.ILiveService;
import com.xishi.liveShow.entity.vo.GiftListVo;
import com.xishi.liveShow.entity.vo.LiveGiftListVo;
import com.xishi.liveShow.model.pojo.Gift;
import com.xishi.liveShow.model.pojo.Live;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.entity.mqMessage.LiveGiftRabbitMessage;
import com.xishi.user.entity.vo.UserWalletVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 礼物 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-12-03
 */
@Service
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements IGiftService {

    @Autowired
    UserService userService;
    @Autowired
    MqLiveShowSender mqLiveShowSender;
    @Autowired
    ILiveService liveService;

    @Override
    public Resp<List<GiftListVo>> getGiftList(GiftListQuery data) {
        List<Gift> gifts = this.list(new QueryWrapper<Gift>().eq("state", 0));
        List<GiftListVo> giftListVos = TransUtil.transList(gifts, GiftListVo.class);
        return Resp.successData(giftListVos);
    }

    @Override
    public Resp<Void> sendGift(SendGiftReq data) {
        Long giftId = data.getGiftId();
        Long liveId = data.getLiveId();
        Integer num = data.getNum();//赠送数量
        Live dbLive = liveService.getById(liveId);
        Gift dbGift = this.getById(giftId);
        if(dbGift == null){
            return Resp.error("礼物不存在");
        }
        if(dbLive == null){
            return Resp.error("直播间不存在");
        }

        //单价
        BigDecimal money = dbGift.getMoney();
        //礼物总价
        BigDecimal giftMoney = money.multiply(BigDecimal.valueOf(num));
        //查验用户账户余额
        Resp<UserWalletVo> userWalletVoResp = userService.checkUserWallet(new Req<Long>(data.getUserId()));
        UserWalletVo userWalletVo = userWalletVoResp.getData();
        BigDecimal gbMoeny = userWalletVo.getGbMoeny();
        if(gbMoeny.compareTo(giftMoney) < 0){
            return Resp.error("钱包余额不足,请先充值");
        }

        //发送赠送礼物MQ消息
        LiveGiftRabbitMessage message = new LiveGiftRabbitMessage();
        message.setId(giftId);
        message.setLiveId(data.getLiveId());
        message.setNum(data.getNum());
        message.setUserId(data.getUserId());
        message.setLinkUid(dbLive.getUserId());
        message.setPrice(money);
        message.setGiftMoney(giftMoney);
        message.setGiftName(dbGift.getName());
        message.setLiveRecordId(dbLive.getNewLiveRecord());
        message.setStreamName(dbLive.getStreamName());
        mqLiveShowSender.sendLiveGiftMessage(message);
        //更新礼物赠送数量
        baseMapper.addUseCount(giftId);
        return Resp.success();
    }

    @Override
    public Resp<List<LiveGiftListVo>> getLiveGiftList(LiveGiftListQuery data) {
        Live byId = liveService.getById(data.getLiveId());
        if(byId == null || byId.getNewLiveRecord() == null){
            return Resp.error("参数错误");
        }
        data.setLiveRecordId(byId.getNewLiveRecord());
        Resp<List<LiveGiftListVo>> liveGiftList = userService.getLiveGiftList(new Req<>(data));
        List<LiveGiftListVo> data1 = liveGiftList.getData();
        return Resp.successData(data1);
    }
}
