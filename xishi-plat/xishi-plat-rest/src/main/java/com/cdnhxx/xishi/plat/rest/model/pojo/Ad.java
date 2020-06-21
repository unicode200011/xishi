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
 * 广告
 * </p>
 *
 * @author lx
 * @since 2019-08-26
 */
@Data
@TableName("eb_ad")
public class Ad extends Model<Ad> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 广告标题
     */
    private String title;
    /**
     * 广告位置 0：首页顶部轮播 1：首页固定广告位 2：直播页左方1 3：直播页左方2 4：直播页右方1 5：直播页右方2 6：启动页  7：引导页
     */
    private String location;
    /**
     * 广告封面
     */
    private String cover;
    /**
     * 简要说明
     */
    private String intro;
    /**
     * 备注
     */
    private String remark;
    /**
     * 广告连接地址
     */
    private String url;
    /**
     * 状态：0：正常 1：禁用
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 点击次数
     */
    @TableField("click_times")
    private Long clickTimes;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
