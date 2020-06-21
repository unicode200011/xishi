package com.xishi.liveShow.dao.mapper;

import com.xishi.liveShow.model.pojo.LiveRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.movie.entity.req.MovieQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
public interface LiveRecordMapper extends BaseMapper<LiveRecord> {

    void changeLivePlayNum(@Param("data") MovieQuery data);

    void changeLiveWatchNum(@Param("data") MovieQuery data);
}
