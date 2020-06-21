package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付方式和支付类型关联表
 * </p>
 *
 * @author LX
 * @since 2020-02-14
 */
@Data
@TableName("eb_pay_and_type")
public class PayAndType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付方式id
     */
    private Integer payId;

    /**
     * 支持支付类型id
     */
    private Integer typeId;

    /**
     * 别名
     */
    private String name;

    /**
     * 途径：0：支付宝 1：微信 3：银行卡
     */
    private Integer way;


}
