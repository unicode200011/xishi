package com.xishi.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.basic.dao.mapper.ProvincesMapper;
import com.xishi.basic.model.pojo.Provinces;
import com.xishi.basic.service.IProvinceService;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvincesMapper, Provinces> implements IProvinceService {

}
