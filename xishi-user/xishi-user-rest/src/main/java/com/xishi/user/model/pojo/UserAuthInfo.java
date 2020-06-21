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
 * 用户实名认证信息
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
@Data
@TableName("eb_user_auth_info")
public class UserAuthInfo extends Model<UserAuthInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 0 待认证 1 认证中 2 已认证 3 认证失败
     */
    private Integer state;

    /**
     * 实名操作业务编号
     */
    @TableField("biz_no")
    private String bizNo;

    /**
     * 实名未通过原因
     */
    private String reason;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    /**
     * 是否已支付,0--未支付，1--已支付
     */
    @TableField("has_pay")
    private Integer hasPay;

    /**
     * 实名认证次数
     */
    @TableField("auth_time")
    private Integer authTime;

    /**
     * 身份证正面
     */
    @TableField("card_front")
    private String cardFront;

    /**
     * 身份证反面
     */
    @TableField("card_back")
    private String cardBack;

    /**
     * 手持身份证
     */
    @TableField("card_hand")
    private String cardHand;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
