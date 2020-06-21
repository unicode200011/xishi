package com.cdnhxx.xishi.plat.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdnhxx.xishi.plat.rest.model.pojo.CommonData;

/**
 * <p>
 * 公共数据 服务类
 * </p>
 *
 * @author lx
 * @since 2019-08-22
 */
public interface ICommonDataService extends IService<CommonData> {

    /**
     * 缓存获取数据
     *
     * @param dataKey
     * @return
     */
    String queryCommonDataFromCache(String dataKey);

    CommonData queryByKey(String key);

    void commonDataToCache(String key, String commonData);

    String queryById(Integer num);
}
