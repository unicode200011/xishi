package com.xishi.movie.model.pojo;

import java.math.BigDecimal;

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
 * 电影
 * </p>
 *
 * @author LX
 * @since 2019-11-11
 */
@Data
@TableName("eb_movie")
public class Movie extends Model<Movie> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 上传渠道 0: 平台 1:代理商
     */
    private Integer source;

    /**
     * 封面图片
     */
    private String cover;

    /**
     * 上架时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 播放总量
     */
    @TableField("play_num")
    private Long playNum;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 电影链接
     */
    private String url;

    /**
     * 电影分类
     */
    @TableField("cate_id")
    private Integer cateId;

    /**
     * 状态 0：上架  1：下架 2：删除
     */
    private Integer state;

    /**
     * 上传人
     */
    @TableField("upload_user")
    private String uploadUser;
    /**
     * 当前观看人数
     */
    @TableField("watch_num")
    private Long watchNum;

    /**
     * 电影简介
     */
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
