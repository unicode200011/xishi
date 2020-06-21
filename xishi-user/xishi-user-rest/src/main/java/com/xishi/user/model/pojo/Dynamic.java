package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 动态列表
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
@Data
@TableName("eb_dynamic")
public class Dynamic extends Model<Dynamic> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 图片 多张逗号隔开
     */
    private String images;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 评论数
     */
    @TableField("comment_num")
    private Long commentNum;

    /**
     * 点赞数
     */
    @TableField("praise_num")
    private Long praiseNum;

    @TableField("video_url")
    private String videoUrl;

    @TableField("video_cover")
    private String videoCover;

    /**
     * 状态 0：正常  1:删除
     */
    private Integer state;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
