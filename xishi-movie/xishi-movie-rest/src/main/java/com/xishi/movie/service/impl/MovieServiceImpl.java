package com.xishi.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.StrKit;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.entity.req.MovieTypeQuery;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.movie.model.pojo.Movie;
import com.xishi.movie.dao.mapper.MovieMapper;
import com.xishi.movie.model.pojo.MovieCate;
import com.xishi.movie.service.IMovieCateService;
import com.xishi.movie.service.IMovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.entity.req.UserSearchQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 电影 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Service
@Slf4j
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements IMovieService {

    @Autowired
    private IMovieCateService movieCateService;

    @Override
    public Resp<List<MovieVo>> getMovies(MovieTypeQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<Movie> wrapper = new QueryWrapper();
        if(data.getType()!=null){
            wrapper.eq("cate_id",data.getType());
        }
        wrapper.eq("state",0);
//        wrapper.orderByDesc("play_num");
        wrapper.orderByDesc("create_time");
        List<Movie> list = this.list(wrapper);
        List<MovieVo> movieVos = TransUtil.transList(list, MovieVo.class);
        for (MovieVo movieVo : movieVos) {
            MovieCate byId = movieCateService.getById(movieVo.getCateId());
            movieVo.setCateName(byId == null?"":byId.getName());
        }
        Resp<List<MovieVo>> resp = new Resp<>(movieVos);
        return resp;
    }

    @Override
    public Resp<Void> changeMoviesPlayNum(MovieQuery data) {
        this.baseMapper.addMoviesPlayNum(data);
        return Resp.success();
    }

    @Override
    public Resp<Void> changeMoviesWatchNum(MovieQuery data) {
        log.info("movie更新数量data：【{}】",data);
        this.baseMapper.addMoviesWatchNum(data);
        return Resp.success();
    }

    @Override
    public Resp<List<MovieVo>> searchMovie(UserSearchQuery query) {
        PageHelper.startPage(query.getPage(),query.getRows());
        QueryWrapper<Movie> wrapper = new QueryWrapper();
        wrapper.eq("state",0);
        if(StrKit.isNotEmpty(query.getKeyword())){
            wrapper.like("name",query.getKeyword());
        }
        wrapper.orderByDesc("play_num");
        List<Movie> list = this.list(wrapper);
        List<MovieVo> movieVos = TransUtil.transList(list, MovieVo.class);
        for (MovieVo movieVo : movieVos) {
            MovieCate byId = movieCateService.getById(movieVo.getCateId());
            movieVo.setCateName(byId == null?"":byId.getName());
        }
        return Resp.successData(movieVos);
    }

    @Override
    public Resp<MovieVo> queryMovieInfo(Long data) {
        Movie byId = this.getById(data);
        MovieVo movieVo = TransUtil.transEntity(byId, MovieVo.class);
        return Resp.successData(movieVo);
    }
}
