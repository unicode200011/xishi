package com.xishi.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.basic.dao.mapper.CitiesMapper;
import com.xishi.basic.model.pojo.Cities;
import com.xishi.basic.service.ICityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl extends ServiceImpl<CitiesMapper, Cities> implements ICityService {

    public List<Cities> queryCityByProvince(String provinceId) {
        List<Cities> list = baseMapper.selectList(new QueryWrapper<Cities>().eq("provinceid",provinceId));
        return list;
    }

}
