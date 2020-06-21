package com.cdnhxx.xishi.plat.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 广告
 *
 * @author: lx
 */
@ApiModel(value = "AdReq", description = "广告详情")
@Data
public class AdReq {

    @ApiModelProperty("编号")
    private Integer type;

    @ApiModelProperty("广告id 详情的时候传")
    private Integer id;

}
