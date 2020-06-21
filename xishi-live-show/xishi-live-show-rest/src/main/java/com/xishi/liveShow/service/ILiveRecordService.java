package com.xishi.liveShow.service;

import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.LiveReq;
import com.xishi.liveShow.entity.vo.LiveDetailVo;
import com.xishi.liveShow.entity.vo.LiveShowDetailVo;
import com.xishi.liveShow.entity.vo.LiveShowVo;
import com.xishi.liveShow.entity.vo.LiveVo;
import com.xishi.liveShow.model.pojo.LiveRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.movie.entity.req.MovieQuery;
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
public interface ILiveRecordService extends IService<LiveRecord> {

    Resp<LiveVo> getLastLiveRecord(Long userId);

    Resp<LiveShowVo> genLiveShow(LiveReq data);

    Resp<List<LiveShowDetailVo>> liveShowDetail(UserSearchQuery query);

    Resp<Void> changeLivePlayNum(MovieQuery data);

    Resp<Void> changeLiveWatchNum(MovieQuery data);

    Resp<LiveDetailVo> getLiveInfo(Long liveId);
}
