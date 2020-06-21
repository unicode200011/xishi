package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.webcore.service.RedisService;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.dao.mapper.NationMapper;
import com.xishi.user.model.pojo.Nation;
import com.xishi.user.service.INationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

    @Autowired
    private RedisService redisService;

    public List<Nation> queryListFromCache() {
        List<Nation> list =null;
        String cacheKey = SystemConstants.NATION_REDIS_KEY;
        Object obj = redisService.getFromCache(cacheKey);
        if (obj == null) {
            list = this.list();
            if(list==null || list.isEmpty()) {
                return null;
            }
            redisService.toCache(cacheKey, list);
        } else {
           list = (List<Nation>)obj;
        }
        return list;
    }
}
