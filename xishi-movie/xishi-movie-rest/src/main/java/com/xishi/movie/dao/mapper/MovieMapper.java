package com.xishi.movie.dao.mapper;

import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.model.pojo.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 电影 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Component
public interface MovieMapper extends BaseMapper<Movie> {

    void addMoviesPlayNum(@Param("data") MovieQuery data);

    void addMoviesWatchNum(@Param("data") MovieQuery data);

}
