package com.xishi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.basic.model.pojo.Cities;

import java.util.List;

public interface ICityService extends IService<Cities> {

    public List<Cities> queryCityByProvince(String provinceId);
}
