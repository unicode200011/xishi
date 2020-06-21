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
 * 邀请码
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@TableName("eb_invitation_code")
@Data
public class InvitationCode extends Model<InvitationCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 邀请码
     */
    private String code;
    /**
     * 二维码
     */
    @TableField("qr_code")
    private String qrCode;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 已邀请人数
     */
    @TableField("user_num")
    private Integer userNum;

    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
