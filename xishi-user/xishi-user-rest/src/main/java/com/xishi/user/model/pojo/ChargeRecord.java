package com.xishi.user.model.pojo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Data
@TableName("eb_charge_record")
public class ChargeRecord extends Model<ChargeRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 充值用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 充值人民币金额
     */
    @TableField("rmb_amount")
    private BigDecimal rmbAmount;

    /**
     * 西施币数量
     */
    @TableField("xishi_amount")
    private BigDecimal xishiAmount;

    /**
     * 充值后钱包余额
     */
    @TableField("wallet_amount")
    private BigDecimal walletAmount;

    /**
     * 充值途径 0微信 1支付宝
     */
    private Integer source;

    /**
     * 充值时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("order_num")
    private String orderNum;

    @TableField("xishi_order_num")
    private String xishiOrderNum;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
