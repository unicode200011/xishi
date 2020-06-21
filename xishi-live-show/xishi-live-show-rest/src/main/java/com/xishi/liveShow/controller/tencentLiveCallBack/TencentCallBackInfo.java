package com.xishi.liveShow.controller.tencentLiveCallBack;

import lombok.Data;

@Data
public class TencentCallBackInfo {
    private String app;
    private long appid;
    private String appname;
    private String channel_id;
    private int errcode;
    private String errmsg;
    private long event_time;
    private int event_type;
    private int idc_id;
    private String node;
    private String push_duration;
    private String sequence;
    private int set_id;
    private String sign;
    private String stream_id;
    private String stream_param;
    private long t;
    private String user_ip;
}
