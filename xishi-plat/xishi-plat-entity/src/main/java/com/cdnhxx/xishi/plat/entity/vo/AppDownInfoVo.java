package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lx
 */
@ApiModel(value = "appDownInfoVo", description = "下载地址")
@Data
public class AppDownInfoVo {

    @ApiModelProperty("ios下载地址")
    private String iosDownUrl;

    @ApiModelProperty("安卓下载地址")
    private String andDownUrl;

    public AppDownInfoVo(String iosDownUrl, String andDownUrl) {
        this.iosDownUrl = iosDownUrl;
        this.andDownUrl = andDownUrl;
    }

    public AppDownInfoVo() {
    }
}
