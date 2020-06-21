package com.xishi.user.entity.mqMessage;


import lombok.Data;

@Data
public class LiveRabbitMessage {
    private String type;
    private Integer code;
    private Long liveId;
    private String streamName;
}
