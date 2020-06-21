package com.xishi.movie.service;

import com.common.base.model.Resp;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.entity.req.MovieTypeQuery;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.movie.model.pojo.Movie;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.entity.req.UserSearchQuery;

import java.util.List;

/**
 * <p>
 * 电影 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
public interface IMovieService extends IService<Movie> {

    Resp<List<MovieVo>> getMovies(MovieTypeQuery data);

    Resp<Void> changeMoviesPlayNum(MovieQuery data);

    Resp<Void> changeMoviesWatchNum(MovieQuery data);

    Resp<List<MovieVo>> searchMovie(UserSearchQuery query);

    Resp<MovieVo> queryMovieInfo(Long data);
}
