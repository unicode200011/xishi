package com.xishi.socket.service.impl;

import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.corundumstudio.socketio.SocketIOClient;
import com.xishi.movie.entity.req.MovieQuery;
import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.socket.Enum.SocketResultTypeEnum;
import com.xishi.socket.entity.vo.SocketResultVo;
import com.xishi.socket.feign.MovieService;
import com.xishi.socket.feign.UserService;
import com.xishi.socket.mq.MqSender;
import com.xishi.socket.service.SocketLoginService;
import com.xishi.socket.service.SocketMovieService;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.MoviePayRabbitMessage;
import com.xishi.user.entity.vo.UserWalletVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class SocketMovieServiceImpl implements SocketMovieService {

    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    MqSender mqSender;
    @Override
    public boolean doSockeMovie(SocketIOClient client, Long userId, Long movieId, String event) {
        //看电影逻辑
        Resp<MovieVo> movieVoResp = movieService.queryMovieInfo(new Req<>(movieId));
        MovieVo movieVo = movieVoResp.getData();
        if(movieVo == null){
            client.sendEvent(event,SocketResultVo.error("参数错误", SocketResultTypeEnum.RESULT_WATCH_MOVIE.getCode()));
        }
        BigDecimal price = movieVo.getPrice();
        if(price.compareTo(BigDecimal.ZERO) > 0){
            Resp<UserWalletVo> userWalletVoResp = userService.checkUserWallet(new Req<>(userId));
            UserWalletVo data = userWalletVoResp.getData();
            BigDecimal gbMoeny = data.getGbMoeny();//钱包余额
            if(price.compareTo(gbMoeny) >0){
                client.sendEvent(event,SocketResultVo.error("钱包余额不足", SocketResultTypeEnum.RESULT_WATCH_MOVIE.getCode()));
                return false;
            }

            boolean exists = redisService.exists(RedisConstants.USER_PAY_MOVIE+userId + movieId);

            if(!exists){
                //发起看电影付费
                MoviePayRabbitMessage moviePayRabbitMessage = new MoviePayRabbitMessage();
                moviePayRabbitMessage.setPrice(price);
                moviePayRabbitMessage.setMovieId(movieId);
                moviePayRabbitMessage.setName(movieVo.getName());
                moviePayRabbitMessage.setUserId(userId);
                mqSender.sendMoviePayMessage(moviePayRabbitMessage);

                redisService.set(RedisConstants.USER_PAY_MOVIE+userId + movieId, 1, 600000L);
            }

        }
        return true;
    }

    @Override
    public void updateMovieWatchNum(Long movieId, Integer roomClientNum,Integer io) {
        MovieQuery movieQuery = new MovieQuery();
        movieQuery.setId(movieId);
        movieQuery.setIo(io);
        movieQuery.setCount(roomClientNum);
        Req<MovieQuery> userReq = new Req<MovieQuery>(movieQuery);
        log.info("更新观看数量MovieQuery==={}",movieQuery);
        movieService.changeMoviesWatchNum(userReq);//更新当前观看人数
        if(io == 0){
            movieService.changeMoviesPlayNum(userReq);//更新播放总量
        }
    }
}
