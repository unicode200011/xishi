package com.xishi.liveShow.model.pojo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LX
 * @since 2019-11-29
 */
@Data
@TableName("eb_live_record")
public class LiveRecord extends Model<Live> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 直播id
     */
    @TableField("live_id")
    private Long liveId;

    /**
     * 直播封面
     */
    private String cover;

    /**
     * 直播标题
     */
    private String name;

    /**
     * 当前观看人数
     */
    @TableField("live_watch_now")
    private Long liveWatchNow;

    /**
     * 本场总管看人数
     */
    @TableField("live_watch_total")
    private Long liveWatchTotal;

    /**
     * 主播名字
     */
    @TableField("shower_name")
    private String showerName;

    /**
     * 已开播时长 分钟
     */
    @TableField("show_time")
    private Long showTime;

    /**
     * 本场直播当前总贡献量
     */
    private BigDecimal amount;

    /**
     * 直播状态 0：正在直播 1：直播结束
     */
    private Integer state;

    /**
     * 直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费
     */
    @TableField("live_mode")
    private Integer liveMode;

    /**
     * 直播收费价格 收费模式有效  
     */
    @TableField("live_price")
    private BigDecimal livePrice;

    @TableField("live_pwd")
    private String livePwd;

    @TableField("create_time")
    private Date createTime;

    @TableField("end_time")
    private Date endTime;
    @TableField("user_id")
    private Long userId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
