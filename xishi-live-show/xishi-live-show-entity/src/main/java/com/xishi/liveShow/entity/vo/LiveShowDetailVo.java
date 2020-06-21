package com.xishi.liveShow.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "LiveShowDetailVo", description = "直播明细信息")
public class LiveShowDetailVo {

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty("直播时长 分钟")
    private Long showTime;
}
