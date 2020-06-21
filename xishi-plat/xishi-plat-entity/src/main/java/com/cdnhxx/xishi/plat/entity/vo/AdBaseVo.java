package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 广告
 *
 * @author: lx
 */
@ApiModel(value = "adBaseVo", description = "广告")
@Data
public class AdBaseVo {

    @ApiModelProperty("广告ID,进入详情用")
    private Long id;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("内容类型: 0 图文 1 连接地址 2 商品")
    private Integer contentType;

    @ApiModelProperty("内容: 连接地址,图文,商品ID等")
    private String content;
}
