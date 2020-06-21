package com.xishi.liveShow.dao.mapper;

import com.xishi.liveShow.model.pojo.Gift;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 礼物 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-12-03
 */
public interface GiftMapper extends BaseMapper<Gift> {

    void addUseCount(Long giftId);
}
