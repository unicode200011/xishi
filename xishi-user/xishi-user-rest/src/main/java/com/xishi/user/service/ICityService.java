package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.model.pojo.Cities;

import java.util.List;

public interface ICityService extends IService<Cities> {

    public List<Cities> queryCityByProvince(String provinceId);
}
