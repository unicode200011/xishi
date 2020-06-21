package com.xishi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.basic.model.pojo.CommonData;

public interface ICommonService extends IService<CommonData> {

    //根据key查询对应的配置信息
    public CommonData queryByKey(String key);

    //缓存中获取数据
    public String queryCommonDataFromCache(String key);

    //清楚数据缓存
    public void cleanDataCache(String key);
}
