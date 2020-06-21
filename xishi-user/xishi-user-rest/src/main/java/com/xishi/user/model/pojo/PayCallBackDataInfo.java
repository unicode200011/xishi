package com.xishi.user.model.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PayCallBackDataInfo {
    private String merchantId;//接入商户标识
    private String timestamp;//请求时间
    private String signatureMethod;//签名类型
    private String signature;//签名
    private String orderId;//商户的订单编号
    private String jOrderId;//商户的订单号
    private String notifyUrl;
    private String currency;//货币类型
    private String payWay;//支付方式
    private String jExtra;//附加字段
    private Long payTime;//支付时间
    private Integer orderType;//订单类型 1充值
    private Integer status;//订单状态
    private Integer signatureVersion;//签名算法版本
    private Double amount;//订单金额
    private Double actualAmount;//实际支付金额
    private Double fee;//产生的费用

    /**
     * add bee 2020/6/20 by unicode
     * 参数名称	参数含义	参与签名	参数说明
     * memberid	商户编号	是
     * orderid	订单号	是
     * amount	订单金额	是
     * transaction_id	交易流水号	是
     * datetime	交易时间	是
     * returncode	交易状态	是	“00” 为成功
     * attach	扩展返回	否	商户附加数据返回
     * sign	签名	否	请看验证签名字段格式
     */
    private String memberid;
    private String transaction_id;
    private String datetime;
    private String returncode;
    private String attach;
    private String sign;

    public PayCallBackDataInfo() {
    }
}
