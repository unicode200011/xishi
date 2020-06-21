package com.cdnhxx.xishi.plat.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "commonDataReq", description = "公共数据请求信息")
public class CommonDataReq {

    @ApiModelProperty(value =
            "公共数据编号 1--用户协议，" +
                    "2--直播运营 3--关于我们, " +
                    "4--直播间公告 5--社区公约 " +
                    "6--隐私政策 7--主播协议" +
                    "8--签约说明" +
                    "9--APP广告投放" +
                    "10--直播间广告 11--充值咨询 " +
                    "12--联系官方"+
                    "14--兑换比例"+
                    "15--推广合作"+
                    "16--相关授权合同"
    )
    private Integer commonNum;

    public CommonDataReq(Integer commonNum) {
        this.commonNum = commonNum;
    }

    public CommonDataReq() {
    }
}
