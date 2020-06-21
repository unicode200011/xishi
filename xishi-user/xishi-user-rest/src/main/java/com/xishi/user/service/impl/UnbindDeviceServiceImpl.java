package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.dao.mapper.UnbindDeviceMapper;
import com.xishi.user.model.pojo.UnbindDevice;
import com.xishi.user.service.IUnbindDeviceService;
import org.springframework.stereotype.Service;

@Service
public class UnbindDeviceServiceImpl extends ServiceImpl<UnbindDeviceMapper,UnbindDevice> implements IUnbindDeviceService {

    public Integer queryUserYearUnbindCount(Long userId,int year) {
        QueryWrapper<UnbindDevice> queryWrapper = new QueryWrapper<UnbindDevice>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("do_year",year);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count;
    }
}
