package com.xishi.user.service;

import com.xishi.user.entity.vo.LiveGiftRankingVo;
import com.xishi.user.model.pojo.WatchRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2020-02-20
 */
public interface IWatchRecordService extends IService<WatchRecord> {

    List<LiveGiftRankingVo> getLiveGiftRanking(Long data,Integer size,List<Long> userIds);
}
