package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PushMessage", description = "推送消息体")
public class PushMessage {
    @ApiModelProperty("被推送的用户名")
    private String pushUserName;

    @ApiModelProperty("被推送的用户id")
    private Long loginUserId;

    @ApiModelProperty("推送内容")
    private String content;
}
