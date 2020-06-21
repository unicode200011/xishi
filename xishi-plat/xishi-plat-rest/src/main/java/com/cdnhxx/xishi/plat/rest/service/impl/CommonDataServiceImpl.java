package com.cdnhxx.xishi.plat.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdnhxx.xishi.plat.rest.dao.mapper.CommonDataMapper;
import com.cdnhxx.xishi.plat.rest.model.pojo.CommonData;
import com.cdnhxx.xishi.plat.rest.service.ICommonDataService;
import com.cdnhxx.xishi.plat.rest.constant.SystemConstants;
import com.cloud.webcore.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公共数据 服务实现类
 * </p>
 *
 * @author lx
 * @since 2019-08-22
 */
@Service
public class CommonDataServiceImpl extends ServiceImpl<CommonDataMapper, CommonData> implements ICommonDataService {

    @Autowired
    private RedisService redisService;

    @Override
    public String queryCommonDataFromCache(String key) {
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + key;
        Object obj = redisService.getFromCache(cacheKey);
        String commonData = "";
        if (obj == null) {
            CommonData commonParam = queryByKey(key);
            commonData = (commonParam == null || commonParam.getValue() == null) ? "" : commonParam.getValue();
            if (StringUtils.isBlank(commonData)) {
                return commonData;
            }
            redisService.toCache(cacheKey, commonData, SystemConstants.COMMEN_DATA_REDIS_KEY_EXPIRE_TIME);
        } else {
            commonData = obj.toString();
        }
        return commonData;
    }

    @Override
    public CommonData queryByKey(String key) {
        CommonData commonParams = baseMapper.selectOne(new QueryWrapper<CommonData>().eq("data_key", key));
        return commonParams;
    }

    @Override
    public void commonDataToCache(String key, String commonData) {
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + key;
        redisService.toCache(cacheKey, commonData, SystemConstants.COMMEN_DATA_REDIS_KEY_EXPIRE_TIME);
    }

    @Override
    public String queryById(Integer num) {
        CommonData commonParams = baseMapper.selectOne(new QueryWrapper<CommonData>().eq("id", num));
        return commonParams.getValue();
    }
}
