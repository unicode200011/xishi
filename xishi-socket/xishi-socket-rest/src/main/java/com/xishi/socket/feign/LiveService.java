package com.xishi.socket.feign;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.socket.entity.vo.LiveDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "xishi-liveShow")
public interface LiveService {
    @PostMapping("/live/changeLivePlayNum")
    Resp<Void> changeLivePlayNum(Req<MovieQuery> req);

    @PostMapping("/live/changeLiveWatchNum")
    Resp<Void> changeLiveWatchNum(Req<MovieQuery> req);

    @PostMapping("/live/getLiveInfo")
    Resp<LiveDetailVo> getLiveInfo(Req<Long> req);
}
