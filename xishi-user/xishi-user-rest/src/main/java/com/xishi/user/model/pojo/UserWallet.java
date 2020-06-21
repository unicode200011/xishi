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
 * 用户账户
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Data
@TableName("eb_user_wallet")
public class UserWallet extends Model<UserWallet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 西施币数量
     */
    @TableField("gb_moeny")
    private BigDecimal gbMoeny;
    /**
     * 合计收到打赏（西施币）
     */
    @TableField("gift_amount")
    private BigDecimal giftAmount;
    /**
     * 消费总值
     */
    @TableField("give_amount")
    private BigDecimal giveAmount;

    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    private Long version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
