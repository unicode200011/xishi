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
 * 
 * </p>
 *
 * @author LX
 * @since 2019-11-26
 */
@Data
@TableName("eb_agent_invite")
public class AgentInvite extends Model<AgentInvite> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 被邀请用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 邀请时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 家族编号
     */
    @TableField("agent_num")
    private String agentNum;

     @TableField("agent_id")
     private Long agentId;

    /**
     * 是否同意：0 未处理 1 已同意 2：已拒绝
     */
    @TableField("join_state")
    private Integer joinState;

    /**
     * 类型 0：用户申请  1：打理人邀请
     */
    private Integer type;

    /**
     * 状态：0：正常 1：已过期
     */
    private Integer state;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
