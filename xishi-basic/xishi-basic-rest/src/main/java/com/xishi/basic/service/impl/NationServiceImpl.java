package com.xishi.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.webcore.service.RedisService;
import com.xishi.basic.dao.mapper.NationMapper;
import com.xishi.basic.model.pojo.Nation;
import com.xishi.basic.service.INationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

    //名族
    public static final String NATION_REDIS_KEY = "nation:list";

    @Autowired
    private RedisService redisService;

    public List<Nation> queryListFromCache() {
        List<Nation> list =null;
        Object obj = redisService.getFromCache(NATION_REDIS_KEY);
        if (obj == null) {
            list = this.list();
            if(list==null || list.isEmpty()) {
                return null;
            }
            redisService.toCache(NATION_REDIS_KEY, list,600);//缓存10分钟
        } else {
           list = (List<Nation>)obj;
        }
        return list;
    }
}
