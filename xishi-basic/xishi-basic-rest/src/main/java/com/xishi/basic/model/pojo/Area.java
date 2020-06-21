package com.xishi.basic.model.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 城市
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@TableName("eb_area")
@Data
public class Area extends Model<Area> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 城市名/省份名
     */
    private String name;
    /**
     * 区域类型 0:省  1:市  2:区县  3:镇
     */
    private Integer type;
    /**
     * 父级ID
     */
    @TableField("parent_id")
    private Long parentId;
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
