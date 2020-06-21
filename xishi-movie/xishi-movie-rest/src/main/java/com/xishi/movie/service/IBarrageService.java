package com.xishi.movie.service;

import com.common.base.model.Resp;
import com.xishi.movie.entity.req.BarrageQuery;
import com.xishi.movie.entity.req.BarrageReq;
import com.xishi.movie.entity.vo.BarrageVo;
import com.xishi.movie.model.pojo.Barrage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 弹幕表 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
public interface IBarrageService extends IService<Barrage> {

    Resp<Void> sendBarrage(BarrageReq data);

    Resp<List<BarrageVo>> getBarrages(BarrageQuery data);
}
