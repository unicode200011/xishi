package com.xishi.basic.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 省份
 * </p>
 *
 * @author stylefeng
 * @since 2018-07-01
 */
@TableName("eb_provinces")
@Data
public class Provinces extends Model<Provinces> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 省份ID
     */
    private String provinceid;
    /**
     * 省份
     */
    private String province;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }



}
