package com.xishi.movie.model.pojo;

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
 * 弹幕表
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Data
@TableName("eb_barrage")
public class Barrage extends Model<MovieCate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 电影id
     */
    @TableField("movie_id")
    private Long movieId;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 时间点 秒
     */
    private Long point;

    @TableField("create_time")
    private Date createTime;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
