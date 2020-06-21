package com.xishi.liveShow.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SortListQuery", description = "直播列表信息")
public class SortListQuery extends BaseQuery {
    @ApiModelProperty("标签 0：收益榜 1：贡献榜")
    private Integer type = 0;

    @ApiModelProperty("0:日榜 1：周榜 2：月榜 3；总榜")
    private Integer sourceType = 0;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;

}
