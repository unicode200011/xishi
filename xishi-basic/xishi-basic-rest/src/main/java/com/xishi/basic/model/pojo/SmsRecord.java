package com.xishi.basic.model.pojo;

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
 * 短信记录
 * </p>
 *
 * @author lx
 * @since 2019-05-20
 */
@TableName("eb_sms_record")
@Data
public class SmsRecord extends Model<SmsRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 手机
     */
    private String phone;
    /**
     * 验证码
     */
    private String code;
    /**
     * 状态 0 成功 1 失败
     */
    private Integer state;
    /**
     * 失败原因
     */
    private String reason;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
