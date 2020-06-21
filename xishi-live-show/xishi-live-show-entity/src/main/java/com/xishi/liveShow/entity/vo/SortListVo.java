package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SortListVo", description = "排行榜列表信息")
public class SortListVo {
    @ApiModelProperty("排行榜用户信息")
    private List<SortUserVo> userInfo;

    @ApiModelProperty("排行榜直播列表信息")
    private List<LiveListVo> liveInfo;
}
