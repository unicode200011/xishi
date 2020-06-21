package com.xishi.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.movie.entity.req.BarrageQuery;
import com.xishi.movie.entity.req.BarrageReq;
import com.xishi.movie.entity.vo.BarrageVo;
import com.xishi.movie.model.pojo.Barrage;
import com.xishi.movie.dao.mapper.BarrageMapper;
import com.xishi.movie.service.IBarrageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 弹幕表 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Service
public class BarrageServiceImpl extends ServiceImpl<BarrageMapper, Barrage> implements IBarrageService {

    @Override
    public Resp<Void> sendBarrage(BarrageReq data) {
        Barrage barrage = TransUtil.transEntity(data, Barrage.class);
        boolean save = this.save(barrage);
        return Resp.success();
    }

    @Override
    public Resp<List<BarrageVo>> getBarrages(BarrageQuery data) {
        PageHelper.startPage(data.page,data.getRows());
        QueryWrapper<Barrage> wrapper = new QueryWrapper();
        wrapper.eq("movie_id",data.getMovieId());
        List<Barrage> list = this.list(wrapper);
        List<BarrageVo> barrageVos = TransUtil.transList(list, BarrageVo.class);
        return new Resp<>(barrageVos);
    }
}
