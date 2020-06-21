package com.cdnhxx.xishi.plat.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "commonDataUpdateReq", description = "公共数据更新信息")
public class CommonDataUpdateReq {

    @ApiModelProperty(value =
            "公共数据编号  18 管家婆")
    private Integer commonNum;

    private String value;

    public CommonDataUpdateReq(Integer commonNum, String value) {
        this.commonNum = commonNum;
        this.value = value;
    }

    public CommonDataUpdateReq() {
    }
}
