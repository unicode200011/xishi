package com.xishi.liveShow.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveListQuery", description = "直播列表信息")
public class LiveListQuery extends BaseQuery {
    @ApiModelProperty("标签 0：直播 1：关注 2：最新 默认直播")
    private Integer type = 0;

    @ApiModelProperty("0:热门房间 1：付费房间 直播标签有效 默认热门")
    private Integer sourceType = 0;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;


}
