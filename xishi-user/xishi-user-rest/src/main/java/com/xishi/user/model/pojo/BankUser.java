package com.xishi.user.model.pojo;

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
 * 用户银行账户信息
 * </p>
 *
 * @author LX
 * @since 2019-12-14
 */
@Data
@TableName("eb_bank_user")
public class BankUser extends Model<BankUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户分类id
     */
    @TableField("bank_account_id")
    private Long bankAccountId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 卡号信息
     */
    @TableField("bank_card")
    private String bankCard;

    /**
     * 二维码
     */
    private String qrcode;

    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
