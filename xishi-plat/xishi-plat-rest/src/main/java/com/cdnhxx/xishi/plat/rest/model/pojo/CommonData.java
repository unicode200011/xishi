package com.cdnhxx.xishi.plat.rest.model.pojo;

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
 * 公共数据
 * </p>
 *
 * @author lx
 * @since 2019-08-22
 */
@Data
@TableName("eb_common_data")
public class CommonData extends Model<CommonData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * key
     */
    @TableField("data_key")
    private String dataKey;

    /**
     * 类型
     */
    private String type;

    /**
     * 值 通过 key 获取唯一数据
     */
    private String value;

    /**
     * 值 描述
     */
    private String description;

    /**
     * 拓展参数
     */
    private String extra;

    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * sys-data适用文本类型 0 ：富文本 1 ：文本域 2 1-99纯数字 3：可带两位小数数字
     */
    @TableField("content_type")
    private Integer contentType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
