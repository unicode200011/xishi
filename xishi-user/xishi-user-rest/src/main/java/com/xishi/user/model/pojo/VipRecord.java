package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * vip充值记录
 * </p>
 *
 * @author lx
 * @since 2019-04-19
 */
@Data
@TableName("eb_vip_record")
public class VipRecord extends Model<VipRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("vip_id")
    private Long vipId;
    private BigDecimal money;
    /**
     * 续费时长,单位月
     */
    @TableField("renew_time")
    private Integer renewTime;
    /**
     * 会员到期时间
     */
    @TableField("vip_end_time")
    private Date vipEndTime;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
