package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告
 *
 * @author: lx
 */
@ApiModel(value = "adDetailVo", description = "广告详情")
@Data
public class AdDetailVo {

    @ApiModelProperty("广告ID,进入详情用")
    private Long id;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("广告连接地址")
    private String url;

    @ApiModelProperty("简要说明")
    private String intro;

    @ApiModelProperty("广告标题")
    private String title;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private String time;


}
