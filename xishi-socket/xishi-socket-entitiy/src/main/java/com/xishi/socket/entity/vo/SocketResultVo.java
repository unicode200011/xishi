package com.xishi.socket.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SocketResultVo" ,description = "socket结果")
public class SocketResultVo {

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer SERVER_ERROR = 500;
    private static final Integer SERVER_MAINTAIN = 502;
    private static final String SERVER_MAINTAIN_MSG = "系统维护中,请稍等再试";

    @ApiModelProperty("响应状态 200:响应成功 其他：响应失败")
    private Integer code = 200;

    @ApiModelProperty("提示信息msg，不管响应正常还是错误，提示信息都在这里")
    private String msg ="";

    @ApiModelProperty("返回时间")
    private String time;

    private Object data;

    @ApiModelProperty("推送类型 0:登录  1 其他操作")
    private Integer type;

    public SocketResultVo(Integer type) {
        this(null, SUCCESS_CODE, "",type);
    }

    public SocketResultVo(Object data,Integer type) {
        this(data, SUCCESS_CODE, "",type);
    }

    public SocketResultVo(Integer code, String msg,Integer type) {
        this(null, code, msg,type);
    }

    public SocketResultVo(Object data, Integer code, String msg,Integer type) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.type = type;
        this.time = String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static SocketResultVo success(Integer type) {
        return new SocketResultVo(null, SUCCESS_CODE, "",type);
    }

    public static SocketResultVo successData(Object data,Integer type) {
        return new SocketResultVo(data, SUCCESS_CODE, "",type);
    }

    public static  SocketResultVo success(String msg,Integer type) {
        return new SocketResultVo(null, SUCCESS_CODE, msg,type);
    }

    public static  SocketResultVo error(String msg,Integer type) {
        return new SocketResultVo(null, SERVER_ERROR, msg,type);
    }

}
