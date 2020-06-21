package com.xishi.user.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 支付记录(支付宝,微信)
 * </p>
 *
 * @author lx
 * @since 2019-02-16
 */
@TableName("eb_pay_record")
@Data
public class PayRecord extends Model<PayRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    /**
     * 项目名称
     */
    private String title;
    /**
     * 三方交易单号
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 平台交易单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 交易金额
     */
    private BigDecimal money;
    /**
     * 回调状态: 0:未回调 1:已回调且验签通过 2 回调但验签失败
     */
    private Integer state;
    /**
     * 类型 0 支付宝 1 微信
     */
    private Integer type;
    /**
     * 验签失败原因
     */
    private String reason;
    /**
     * 回调地址
     */
    @TableField("notify_url")
    private String notifyUrl;
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间/回调时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField("gb_money")
    private BigDecimal gbMoney;

    @TableField("gift_money")
    private BigDecimal giftMoney;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

