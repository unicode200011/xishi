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
 * 账户分类
 * </p>
 *
 * @author LX
 * @since 2019-12-14
 */
@Data
@TableName("eb_bank_account")
public class BankAccount extends Model<BankAccount> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账户类型
     */
    private String name;

    /**
     * 账户号码 是否开启：0：未开启 1：开启
     */
    private Integer acount;

    /**
     * 账户二维码 是否开启：0：未开启 1：开启
     */
    private Integer code;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
