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
 * 动态评论表
 * </p>
 *
 * @author LX
 * @since 2019-11-18
 */
@Data
@TableName("eb_dynamic_comment")
public class DynamicComment extends Model<DynamicComment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论id  不为0即为回复
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 动态id
     */
    @TableField("dynamic_id")
    private Long dynamicId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 评论时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态 0：正常  1：删除
     */
    private Integer state;

    /**
     * 备用字段
     */
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
