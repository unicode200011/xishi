package com.xishi.liveShow.controller;


import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.GiftListQuery;
import com.xishi.liveShow.entity.req.LiveGiftListQuery;
import com.xishi.liveShow.entity.req.SendGiftReq;
import com.xishi.liveShow.entity.vo.GiftListVo;
import com.xishi.liveShow.entity.vo.LiveGiftListVo;
import com.xishi.liveShow.service.IGiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/gift")
@Api(value = "直播-礼物相关接口",description = "直播-礼物相关接口")
public class LiveShowGiftController {

    @Autowired
    private IGiftService giftService;


    @PostMapping("/getGiftList")
    @ApiOperation("获取礼物列表数据")
    @NeedLogin
    public Resp<List<GiftListVo>> getGiftList(@RequestBody Req<GiftListQuery> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        GiftListQuery data = req.getData();
        data.setUserId(userId);
        return giftService.getGiftList(data);
    }


    @PostMapping("/sendGift")
    @ApiOperation("赠送礼物")
    @NeedLogin
    public Resp<Void> sendGift(@RequestBody Req<SendGiftReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        SendGiftReq data = req.getData();
        data.setUserId(userId);
        return giftService.sendGift(data);
    }


    @PostMapping("/getLiveGiftList")
    @ApiOperation("获取直播贡献榜")
    @NeedLogin
    public Resp<List<LiveGiftListVo>> getLiveGiftList(@RequestBody Req<LiveGiftListQuery> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveGiftListQuery data = req.getData();
        data.setUserId(userId);
        return giftService.getLiveGiftList(data);
    }



}
