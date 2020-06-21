package com.xishi.liveShow.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GiftListQuery", description = "礼物列表信息")
public class GiftListQuery extends BaseQuery {

    @ApiModelProperty("用户id APP不用传")
    private Long userId;

}
