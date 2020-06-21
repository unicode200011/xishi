package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.dao.mapper.ProvincesMapper;
import com.xishi.user.model.pojo.Provinces;
import com.xishi.user.service.IProvinceService;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvincesMapper, Provinces> implements IProvinceService {

}
