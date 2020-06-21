package com.xishi.liveShow.entity.vo;

import com.xishi.movie.entity.vo.MovieVo;
import com.xishi.user.entity.vo.UserInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "SearchVo", description = "搜索结果")
public class SearchVo {
   @ApiModelProperty("用户列表")
   private List<UserInfoVo> userInfoVos = new ArrayList<>();
   @ApiModelProperty("电影列表")
   private List<MovieVo> movieVos = new ArrayList<>();
   @ApiModelProperty("直播列表 ")
   private List<LiveListVo> liveVos = new ArrayList<>();
}
