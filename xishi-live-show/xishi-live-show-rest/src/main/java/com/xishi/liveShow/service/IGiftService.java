package com.xishi.liveShow.service;

import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.GiftListQuery;
import com.xishi.liveShow.entity.req.LiveGiftListQuery;
import com.xishi.liveShow.entity.req.SendGiftReq;
import com.xishi.liveShow.entity.vo.GiftListVo;
import com.xishi.liveShow.entity.vo.LiveGiftListVo;
import com.xishi.liveShow.model.pojo.Gift;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 礼物 服务类
 * </p>
 *
 * @author LX
 * @since 2019-12-03
 */
public interface IGiftService extends IService<Gift> {

    Resp<List<GiftListVo>> getGiftList(GiftListQuery data);

    Resp<Void> sendGift(SendGiftReq data);

    Resp<List<LiveGiftListVo>> getLiveGiftList(LiveGiftListQuery data);
}
