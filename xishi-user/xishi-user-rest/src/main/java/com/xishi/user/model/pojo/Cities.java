package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 城市
 * </p>
 *
 * @author stylefeng
 * @since 2018-07-02
 */
@TableName("eb_cities")
@Data
public class Cities extends Model<Cities> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String cityid;
    /**
     * 城市名
     */
    private String city;
    /**
     * 省份ID
     */
    private String provinceid;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
