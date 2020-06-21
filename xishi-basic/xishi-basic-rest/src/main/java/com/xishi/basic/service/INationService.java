package com.xishi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.basic.model.pojo.Nation;

import java.util.List;

public interface INationService extends IService<Nation> {

    public List<Nation> queryListFromCache();
}
