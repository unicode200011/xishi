package com.xishi.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.movie.entity.req.MovieTypeQuery;
import com.xishi.movie.entity.vo.MovieCateVo;
import com.xishi.movie.model.pojo.MovieCate;
import com.xishi.movie.dao.mapper.MovieCateMapper;
import com.xishi.movie.service.IMovieCateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Service
public class MovieCateServiceImpl extends ServiceImpl<MovieCateMapper, MovieCate> implements IMovieCateService {

    @Override
    public Resp<List<MovieCateVo>> getMovieTypes(MovieTypeQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<MovieCate> wrapper = new QueryWrapper();
        wrapper.orderByDesc("num");
        List<MovieCate> list = this.list(wrapper);
        List<MovieCateVo> movieCateVos = TransUtil.transList(list, MovieCateVo.class);
        Resp<List<MovieCateVo>> resp = new Resp<>(movieCateVos);
        return resp;
    }
}
