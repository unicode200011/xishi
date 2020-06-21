package com.xishi.socket.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ApiModel(value = "SocketLiveCurrentDataVo",description = "实时更新数据")
public class SocketLiveCurrentDataVo {

    @ApiModelProperty("礼物贡献总值")
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty("直播观看人数")
    private Integer userNum = 0;

    @ApiModelProperty("观看用户")
    private Set<SocketUserVo> userVos = new HashSet<>();
}
