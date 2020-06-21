package com.xishi.user.dao.mapper;

import com.xishi.user.entity.vo.LiveGiftListVo;
import com.xishi.user.entity.vo.LiveGiftRankingVo;
import com.xishi.user.model.pojo.UserWalletRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户钱包记录 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface UserWalletRecordMapper extends BaseMapper<UserWalletRecord> {

    List<LiveGiftListVo> getLiveGiftList(Long liveRecordId);

    List<LiveGiftRankingVo> getLiveGiftRanking(Long data);
}
