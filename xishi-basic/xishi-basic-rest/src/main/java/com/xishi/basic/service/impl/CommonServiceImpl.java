package com.xishi.basic.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.webcore.service.RedisService;
import com.xishi.basic.config.SystemConfig;
import com.xishi.basic.constant.SystemConstants;
import com.xishi.basic.dao.mapper.CommonDataMapper;
import com.xishi.basic.model.pojo.CommonData;
import com.xishi.basic.service.ICommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl extends ServiceImpl<CommonDataMapper, CommonData> implements ICommonService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemConfig systemConfig;

    //缓存中获取数据
    public String queryCommonDataFromCache(String key) {
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + key;
        Object obj = redisService.getFromCache(cacheKey);
        String commonData="";
        if (obj == null) {
            CommonData commonParam = queryByKey(key);
            commonData= (commonParam==null || commonParam.getValue()== null) ?"":commonParam.getValue();
            if(StringUtils.isBlank(commonData)) {
                return commonData;
            }
            redisService.toCache(cacheKey, commonData, systemConfig.getCommonDataExpireTime());
        } else {
            commonData = obj.toString();
        }
        return commonData;
    }

    public CommonData queryByKey(String key) {
        CommonData commonParams =baseMapper.selectOne(new QueryWrapper<CommonData>().eq("data_key",key));
        return commonParams;
    }

    //清楚数据缓存
    public void cleanDataCache(String key) {
        String cacheKey=SystemConstants.COMMEN_DATA_REDIS_KEY + key;
        redisService.remove(cacheKey);
    }

}
