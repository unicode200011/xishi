package com.xishi.basic.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "commonDataReq", description = "公共数据请求信息")
public class CommonDataReq {

    @ApiModelProperty("公共数据编号 1--用户协议，2--手续费说明，3--交换看豆说明")
    private Integer commonNum;
}
