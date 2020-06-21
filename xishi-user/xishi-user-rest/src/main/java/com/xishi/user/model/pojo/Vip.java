package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 会员
 * </p>
 *
 * @author lx
 * @since 2019-02-15
 */
@TableName("eb_vip")
@Data
public class Vip extends Model<Vip> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员名称
     */
    private String name;
    /**
     * 会员等级 0 普通会员 1 荣誉会员
     */
    private Integer num;
    /**
     * 描述
     */
    private String intro;

    /**
     * 月价格
     */
    @TableField("month_money")
    private BigDecimal monthMoney;
    /**
     * 季度价格
     */
    @TableField("quarter_money")
    private BigDecimal quarterMoney;

    /**
     * 到期时间
     */
    @TableField(exist = false)
    private String endTime = "";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
