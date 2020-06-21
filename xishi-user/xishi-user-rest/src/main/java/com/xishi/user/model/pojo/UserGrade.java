package com.xishi.user.model.pojo;

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
 * 会员等级
 * </p>
 *
 * @author LX
 * @since 2019-11-19
 */
@Data
@TableName("eb_user_grade")
public class UserGrade extends Model<UserGrade> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 会员等级
     */
    private Integer level;

    /**
     * 等级标识
     */
    private String image;

    /**
     * 经验值
     */
    private BigDecimal amount;

    /**
     * 等级颜色
     */
    private String color;
    private String rgb;


    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("level_name")
    private String levelName;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
