package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveGiftListQuery", description = "礼物列表信息")
public class LiveGiftListQuery extends BaseQuery {

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("直播记录id  APP不用传")
    private Long liveRecordId;

}
