package com.xishi.movie.service;

import com.common.base.model.Resp;
import com.xishi.movie.entity.req.MovieTypeQuery;
import com.xishi.movie.entity.vo.MovieCateVo;
import com.xishi.movie.model.pojo.MovieCate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
public interface IMovieCateService extends IService<MovieCate> {

    Resp<List<MovieCateVo>> getMovieTypes(MovieTypeQuery data);
}
