package com.xishi.liveShow.feign;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.user.entity.req.UserSearchQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "xishi-movie")
public interface MovieService {

    @PostMapping("/movie/searchMovie")
    Resp<List<MovieVo>> searchMovie(Req<UserSearchQuery> req);
}
