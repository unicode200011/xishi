package com.xishi.user.entity.mqMessage;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentMoneyRabbitMessage {

    @ApiModelProperty("是否只添加钱包记录0:增加金额并记录 1 只增加金额  2：只增加记录")
    private Integer messageType = 0;

    @ApiModelProperty("消息类型 2.获得礼物 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号")
    private Integer type;

    @ApiModelProperty("服务类型 0.获得礼物 1收费直播")
    private Integer serviceType;

    @ApiModelProperty("提成金额")
    private BigDecimal rateMoney;

    @ApiModelProperty("代理商id")
    private Long agentId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("额外数据")
    private String data;
}
