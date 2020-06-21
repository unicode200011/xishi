package com.xishi.liveShow.service;

import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.LiveListQuery;
import com.xishi.liveShow.entity.req.LiveRoomReq;
import com.xishi.liveShow.entity.req.SendMessageReq;
import com.xishi.liveShow.entity.req.SortListQuery;
import com.xishi.liveShow.entity.vo.*;
import com.xishi.liveShow.model.pojo.Live;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.entity.req.UserSearchQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
public interface ILiveService extends IService<Live> {

    Resp<Live> checkLive(Long userId);

    Resp<List<LiveListVo>> getLiveList(LiveListQuery data);

    Resp<List<LiveListVo>> getSortList(SortListQuery data);

    Resp<List<SortUserVo>> getSortUserList(SortListQuery data);

    Resp<LiveShowPullVo> viewLiveShow(LiveRoomReq data);

    void discBack(String stream_id);

    void pushBack(String stream_id);

    Resp<SearchVo> searchLive(UserSearchQuery query);

    Resp<Void> changeMode(LiveRoomReq data);

    Resp<LiveEndDataReq> endLiveShowData(LiveRoomReq data);

    Resp<Void> stopSendMessage(SendMessageReq data);

    Resp<LiveCheckPayVo> checkLivePay(LiveRoomReq data);
}
