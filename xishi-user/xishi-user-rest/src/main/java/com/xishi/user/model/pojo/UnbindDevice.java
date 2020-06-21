package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("eb_unbind_device")
@Data
public class UnbindDevice  extends Model<UnbindDevice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 设备唯一标识
     */
    @TableField("device_token")
    private String deviceToken;
    /**
     * 年
     */
    @TableField("do_year")
    private Integer doYear;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

}