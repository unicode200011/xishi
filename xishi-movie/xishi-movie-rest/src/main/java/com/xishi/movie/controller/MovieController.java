package com.xishi.movie.controller;


import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.movie.entity.req.BarrageQuery;
import com.xishi.movie.entity.req.BarrageReq;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.entity.req.MovieTypeQuery;
import com.xishi.movie.entity.vo.BarrageVo;
import com.xishi.movie.entity.vo.MovieCateVo;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.movie.service.IBarrageService;
import com.xishi.movie.service.IMovieCateService;
import com.xishi.movie.service.IMovieService;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.req.UserSearchQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/movie")
@Api(value = "电影相关接口",description = "电影相关接口")
public class MovieController {


    @Autowired
    private IMovieCateService movieCateService;

    @Autowired
    private IMovieService movieService;
    @Autowired
    private IBarrageService barrageService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/getMovieTypes")
    @ApiOperation("电影分类列表")
    public Resp<List<MovieCateVo>> getMovieTypes(@RequestBody Req<MovieTypeQuery> req){
        MovieTypeQuery data = req.getData();
        return  movieCateService.getMovieTypes(data);
    }

    @PostMapping("/getMovies")
    @ApiOperation("电影列表")
    public Resp<List<MovieVo>> getMovies(@RequestBody Req<MovieTypeQuery> req){
        MovieTypeQuery data = req.getData();
        return  movieService.getMovies(data);
    }



    @PostMapping("/changeMoviesPlayNum")
    @ApiOperation("内部接口--增加/减少电影播放量")
    @InnerInvoke
    public Resp<Void> changeMoviesPlayNum(@RequestBody Req<MovieQuery> req){
        MovieQuery data = req.getData();
        return  movieService.changeMoviesPlayNum(data);
    }

    @PostMapping("/changeMoviesWatchNum")
    @ApiOperation("内部接口--增加/减少电影当前观看人数")
    @InnerInvoke
    public Resp<Void> changeMoviesWatchNum(@RequestBody Req<MovieQuery> req){
        MovieQuery data = req.getData();
        return  movieService.changeMoviesWatchNum(data);
    }

    @PostMapping("/queryMovieInfo")
    @ApiOperation("内部接口--增加/减少电影当前观看人数")
    @InnerInvoke
    public Resp<MovieVo> queryMovieInfo(@RequestBody Req<Long> req){
        Long data = req.getData();
        return  movieService.queryMovieInfo(data);
    }


    @PostMapping("/sendBarrage")
    @ApiOperation("发送弹幕")
    @NeedLogin
    public Resp<Void> sendBarrage(@RequestBody Req<BarrageReq> req){
        Long userId = LoginContext.getLoginUser().getUserId();
        BarrageReq data = req.getData();
        data.setUserId(userId);
        return  barrageService.sendBarrage(data);
    }

    @PostMapping("/getBarrages")
    @ApiOperation("获取弹幕列表")
    public Resp<List<BarrageVo>> getBarrages(@RequestBody Req<BarrageQuery> req){
        BarrageQuery data = req.getData();
        return  barrageService.getBarrages(data);
    }

    @ApiOperation("搜索电影--内部接口,app或其他系统别调")
    @PostMapping("/searchMovie")
    @InnerInvoke
    public Resp<List<MovieVo>> searchMovie(@RequestBody Req<UserSearchQuery> req) {
        UserSearchQuery query = req.getData();
        return movieService.searchMovie(query);
    }

    @ApiOperation("查询电影是否付费 只需传入电影id")
    @PostMapping("/searchMoviePay")
    public Resp<Boolean> searchMoviePay(@RequestBody Req<MovieQuery> req) {
        Long userId = LoginContext.getLoginUser().getUserId();
        Long id = req.getData().getId();
        System.out.println(userId);
        boolean exists = redisService.exists(RedisConstants.USER_PAY_MOVIE+userId + id);
        return Resp.successData(exists);
    }


}
