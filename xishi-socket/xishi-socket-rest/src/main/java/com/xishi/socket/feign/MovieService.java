package com.xishi.socket.feign;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.entity.vo.MovieVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "xishi-movie")
public interface MovieService {
    @PostMapping("/movie/changeMoviesPlayNum")
    Resp<Void> changeMoviesPlayNum(Req<MovieQuery> req);

    @PostMapping("/movie/changeMoviesWatchNum")
    Resp<Void> changeMoviesWatchNum(Req<MovieQuery> req);

    @PostMapping("/movie/queryMovieInfo")
    Resp<MovieVo> queryMovieInfo(Req<Long> req);

    @PostMapping("/live/changeLivePlayNum")
    Resp<Void> changeLivePlayNum(Req<MovieQuery> req);

    @PostMapping("/live/changeLiveWatchNum")
    Resp<Void> changeLiveWatchNum(Req<MovieQuery> req);

}
