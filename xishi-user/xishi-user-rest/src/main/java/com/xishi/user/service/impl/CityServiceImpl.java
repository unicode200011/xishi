package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.dao.mapper.CitiesMapper;
import com.xishi.user.model.pojo.Cities;
import com.xishi.user.service.ICityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl extends ServiceImpl<CitiesMapper, Cities> implements ICityService {

    public List<Cities> queryCityByProvince(String provinceId) {
        List<Cities> list = baseMapper.selectList(new QueryWrapper<Cities>().eq("provinceid",provinceId));
        return list;
    }

}
