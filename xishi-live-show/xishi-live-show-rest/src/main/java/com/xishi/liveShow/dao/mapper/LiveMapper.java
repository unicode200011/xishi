package com.xishi.liveShow.dao.mapper;

import com.xishi.liveShow.entity.req.LiveListQuery;
import com.xishi.liveShow.entity.req.SortListQuery;
import com.xishi.liveShow.entity.vo.LiveEndDataReq;
import com.xishi.liveShow.entity.vo.LiveListVo;
import com.xishi.liveShow.entity.vo.SortUserVo;
import com.xishi.liveShow.model.pojo.Live;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
@Component
public interface LiveMapper extends BaseMapper<Live> {

    List<LiveListVo> getLiveList(@Param("data") LiveListQuery data);

    List<SortUserVo> getSortUserList(@Param("data") SortListQuery data);

    List<LiveListVo> getSortLiveList(@Param("userIds") List<Long> userIds);

    List<LiveListVo> searchLive(@Param("keyword") String keyword);

    LiveEndDataReq getLiveData(Long newLiveRecord);
}
