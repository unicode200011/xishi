package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lx
 */
@ApiModel(value = "versionInfoVo", description = "版本信息")
@Data
public class VersionInfoVo {

    @ApiModelProperty("最低版本")
    private String minV;

    @ApiModelProperty("最高版本")
    private String maxV;

    @ApiModelProperty("更新描述")
    private String desc;

    @ApiModelProperty("下载地址")
    private String downUrl;

    public VersionInfoVo(String minV, String maxV, String desc, String downUrl) {
        this.minV = minV;
        this.maxV = maxV;
        this.desc = desc;
        this.downUrl = downUrl;
    }

    public VersionInfoVo() {
    }
}
