package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.model.pojo.UnbindDevice;

public interface IUnbindDeviceService extends IService<UnbindDevice>{

    public Integer queryUserYearUnbindCount(Long userId,int year);
}
