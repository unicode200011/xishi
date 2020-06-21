package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 银行卡
 * </p>
 *
 * @author lx
 * @since 2018-10-13
 */
@TableName("eb_bank_card")
@Data
public class BankCard extends Model<BankCard> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    /**
     * 持卡人姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行卡logo
     */
    @TableField("bank_logo")
    private String bankLogo;
    /**
     * 银行卡号
     */
    @TableField("card_num")
    private String cardNum;
    /**
     * 卡类型 0:储存卡 1:信用卡
     */
    @TableField("card_type")
    private Integer cardType;
    /**
     * 开户行
     */
    @TableField("open_bank")
    private String openBank;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
