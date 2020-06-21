package com.xishi.user.service.impl;

import com.xishi.user.entity.vo.LiveGiftRankingVo;
import com.xishi.user.model.pojo.WatchRecord;
import com.xishi.user.dao.mapper.WatchRecordMapper;
import com.xishi.user.service.IWatchRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2020-02-20
 */
@Service
public class WatchRecordServiceImpl extends ServiceImpl<WatchRecordMapper, WatchRecord> implements IWatchRecordService {

    @Override
    public List<LiveGiftRankingVo> getLiveGiftRanking(Long data , Integer size,List<Long> userIds) {
        return baseMapper.getLiveGiftRanking(data,size);
    }
}
