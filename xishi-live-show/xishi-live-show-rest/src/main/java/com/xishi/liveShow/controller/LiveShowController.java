package com.xishi.liveShow.controller;


import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.*;
import com.xishi.liveShow.service.ILiveRecordService;
import com.xishi.liveShow.service.ILiveService;
import com.xishi.liveShow.entity.vo.*;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.user.entity.req.UserSearchQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/live")
@Api(value = "直播相关接口",description = "直播相关接口")
public class LiveShowController {

    @Autowired
    private ILiveRecordService liveRecordService;
    @Autowired
    private ILiveService liveService;

    @PostMapping("/genLiveShow")
    @ApiOperation("创建房间")
    @NeedLogin
    public Resp<LiveShowVo> genLiveShow(@RequestBody Req<LiveReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveReq data = req.getData();
        data.setUserId(userId);
        return liveRecordService.genLiveShow(data);
    }

    @PostMapping("/getLastLiveRecord")
    @ApiOperation("获取上次直播数据")
    @NeedLogin
    public Resp<LiveVo> getLastLiveRecord(@RequestBody Req<Void> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        return liveRecordService.getLastLiveRecord(userId);
    }

    @PostMapping("/getLiveList")
    @ApiOperation("获取首页直播列表数据")
    @NeedLogin
    public Resp<List<LiveListVo>> getLiveList(@RequestBody Req<LiveListQuery> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveListQuery data = req.getData();
        data.setUserId(userId);
        return liveService.getLiveList(data);
    }

    @PostMapping("/getSortUserList")
    @ApiOperation("获取排行榜用户数据")
    @NeedLogin
    public Resp<List<SortUserVo>> getSortUserList(@RequestBody Req<SortListQuery> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        SortListQuery data = req.getData();
        data.setUserId(userId);
        return liveService.getSortUserList(data);
    }
    @PostMapping("/getSortLiveList")
    @ApiOperation("获取排行榜直播列表数据")
    @NeedLogin
    public Resp<List<LiveListVo>> getSortLiveList(@RequestBody Req<SortListQuery> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        SortListQuery data = req.getData();
        data.setUserId(userId);
        return liveService.getSortList(data);
    }

    @PostMapping("/viewLiveShow")
    @ApiOperation("用户进入直播间前--获取播流地址，用户鉴权等")
    @NeedLogin
    public Resp<LiveShowPullVo> viewLiveShow(@RequestBody Req<LiveRoomReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveRoomReq data = req.getData();
        data.setUserId(userId);
        return liveService.viewLiveShow(data);
    }

    @PostMapping("/checkLivePay")
    @ApiOperation("用户进入直播间前--查询常规模式下")
    @NeedLogin
    public Resp<LiveCheckPayVo> checkLivePay(@RequestBody Req<LiveRoomReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveRoomReq data = req.getData();
        data.setUserId(userId);
        return liveService.checkLivePay(data);
    }

    @PostMapping("/changeMode")
    @ApiOperation("修改直播模式")
    @NeedLogin
    public Resp<Void> changeMode(@RequestBody Req<LiveRoomReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveRoomReq data = req.getData();
        data.setUserId(userId);
        return liveService.changeMode(data);
    }
    @PostMapping("/stopSendMessage")
    @ApiOperation("全员禁言")
    @NeedLogin
    public Resp<Void> stopSendMessage(@RequestBody Req<SendMessageReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        SendMessageReq data = req.getData();
        data.setUserId(userId);
        return liveService.stopSendMessage(data);
    }

    @PostMapping("/endLiveShowData")
    @ApiOperation("获取直播结束信息")
    @NeedLogin
    public Resp<LiveEndDataReq> endLiveShowData(@RequestBody Req<LiveRoomReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        LiveRoomReq data = req.getData();
        data.setUserId(userId);
        return liveService.endLiveShowData(data);
    }

    @ApiOperation("搜索")
    @PostMapping("/searchLive")
    @NeedLogin
    public Resp<SearchVo> searchLive(@RequestBody Req<UserSearchQuery> req) {
        Long userId = LoginContext.getLoginUser().getUserId();
        UserSearchQuery query = req.getData();
        query.setUserId(userId);
        return liveService.searchLive(query);
    }

    @ApiOperation("用户直播明细")
    @PostMapping("/liveShowDetail")
    @NeedLogin
    public Resp<List<LiveShowDetailVo>> liveShowDetail(@RequestBody Req<UserSearchQuery> req) {
        Long userId = LoginContext.getLoginUser().getUserId();
        UserSearchQuery query = req.getData();
        query.setUserId(userId);
        return liveRecordService.liveShowDetail(query);
    }



    @PostMapping("/changeLivePlayNum")
    @ApiOperation("内部接口--增加/减少直播观看量")
    @InnerInvoke
    public Resp<Void> changeLivePlayNum(@RequestBody Req<MovieQuery> req){
        MovieQuery data = req.getData();
        return  liveRecordService.changeLivePlayNum(data);
    }

    @PostMapping("/changeLiveWatchNum")
    @ApiOperation("内部接口--增加/减少当前直播观看量")
    @InnerInvoke
    public Resp<Void> changeLiveWatchNum(@RequestBody Req<MovieQuery> req){
        MovieQuery data = req.getData();
        return  liveRecordService.changeLiveWatchNum(data);
    }

    @PostMapping("/getLiveInfo")
    @ApiOperation("内部接口--增加/减少当前直播观看量")
    @InnerInvoke
    public Resp<LiveDetailVo> getLiveInfo(@RequestBody Req<Long> req){
        Long liveId = req.getData();
        return  liveRecordService.getLiveInfo(liveId);
    }

}
