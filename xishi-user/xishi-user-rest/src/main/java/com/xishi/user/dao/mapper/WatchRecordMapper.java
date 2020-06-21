package com.xishi.user.dao.mapper;

import com.xishi.user.entity.vo.LiveGiftRankingVo;
import com.xishi.user.model.pojo.WatchRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2020-02-20
 */
public interface WatchRecordMapper extends BaseMapper<WatchRecord> {

    List<LiveGiftRankingVo> getLiveGiftRanking(@Param("data") Long data,@Param("size") Integer size);
}
