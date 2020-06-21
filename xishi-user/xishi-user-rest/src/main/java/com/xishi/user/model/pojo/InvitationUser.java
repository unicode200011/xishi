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
 * 邀请用户列表
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@TableName("eb_invitation_user")
@Data
public class InvitationUser extends Model<InvitationUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("from_uid")
    private Long fromUid;
    /**
     * 被邀请用户
     */
    @TableField("to_uid")
    private Long toUid;
    /**
     * 使用的邀请码
     */
    private String code;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private Integer authed;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
