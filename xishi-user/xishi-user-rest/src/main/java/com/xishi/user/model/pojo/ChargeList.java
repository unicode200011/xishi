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
 * 充值列表
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Data
@TableName("eb_charge_list")
public class ChargeList extends Model<ChargeList> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 金豆数量
     */
    @TableField("xishi_num")
    private Integer xishiNum;

    /**
     * 所需人民币
     */
    private BigDecimal rmb;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 赠送比例
     */
    private BigDecimal rate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
