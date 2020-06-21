package com.xishi.liveShow.model.pojo;

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
@TableName("eb_live")
public class Live extends Model<Live> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 房间号
     */
    @TableField("room_num")
    private String roomNum;

    /**
     * 直播流名
     */
    @TableField("stream_name")
    private String streamName;

    /**
     * 推流地址
     */
    @TableField("push_url")
    private String pushUrl;

    /**
     * 播流地址
     */
    @TableField("pull_url")
    private String pullUrl;

    /**
     * 直播总时长 分钟数
     */
    @TableField("total_live_time")
    private Long totalLiveTime;

    /**
     * 直播历史总观看人数
     */
    @TableField("total_live_watch")
    private Long totalLiveWatch;

    /**
     * 最新直播开始时间
     */
    @TableField("live_start_time")
    private Date liveStartTime;

    /**
     * 最新直播记录id
     */
    @TableField("new_live_record")
    private Long newLiveRecord;

    /**
     * 直播房间状态  0：正常  1：禁播 
     */
    private Integer state;

    /**
     * 直播状态 0：未直播 1：正在直播
     */
    @TableField("live_state")
    private Integer liveState;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
