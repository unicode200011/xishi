package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户签到
 * </p>
 *
 * @author lx
 * @since 2019-02-28
 */
@TableName("eb_user_sign")
@Data
public class UserSign extends Model<UserSign> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    /**
     * 签到日期(yyyy-MM-dd)
     */
    @TableField("sign_date")
    private Date signDate;
    /**
     * 连续签到时间(天)
     */
    @TableField("continuous_day")
    private Integer continuousDay;
    /**
     * 签到所得牛气值(整数)
     */
    private Integer nq;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
