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
 * 代理商处理记录
 * </p>
 *
 * @author LX
 * @since 2019-11-26
 */
@Data
@TableName("eb_agent_deal_record")
public class AgentDealRecord extends Model<AgentDealRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 代理商id
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 处理人
     */
    @TableField("admin_id")
    private Integer adminId;

    /**
     * 处理备注
     */
    private String remark;

    /**
     * 申请时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 处理时间
     */
    @TableField("update_time")
    private Date updateTime;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
