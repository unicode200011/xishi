package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-14
 */
@TableName("eb_user")
@Data
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户手机号/登录名
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * salt
     */
    private String salt;
    /**
     * 昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 区县
     */
    private String county;

    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;
    /**
     * 性别: 0:男 1:女  2:保密
     */
    private Integer gender;
    /**
     * 年龄
     */
    private Integer age;

    private String ip;
    /**
     * 个人简介
     */
    private String intro;
    /**
     * 支付密码
     */
    @TableField("pay_pwd")
    private String payPwd;


    /**
     * 用户总状态 0:正常 1:冻结  2:删除
     */
    private Integer state;
    /**
     * QQ
     */
    private String qq;
    /**
     * VX
     */
    private String wx;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 用户来源: 0 注册  1 系统添加
     */
    private Integer source;
    /**
     * 设备号
     */
    private String machine;
    /**
     * 最后登录时间
     */
    @TableField("login_time")
    private Date loginTime;
    /**
     * 注册时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 热恋吧id
     */
    @TableField("xishi_num")
    private String xishiNum;
    /**
     * 用户在线状态0:离线,1:在线 2:忙碌
     */
    @TableField("online_state")
    private Integer onlineState;
    /**
     * 权重顺序
     */
    @TableField("weight_sort")
    private Integer weightSort;
    /**
     * 是否推荐1是0否
     */
    private Integer recommended;
    /**
     * 所属家族
     */
    @TableField("belong_agent")
    private Long belongAgent;
    /**
     * 主播认证申请状态:1:已申请2:未申请3:拒绝4:通过
     */
    @TableField("apply_status")
    private Integer applyStatus;
    /**
     * 注册方式 0：手机 1：微信 2：qq
     */
    @TableField("registered_state")
    private Integer registeredState;
    /**
     * 主播认证通过时间
     */
    @TableField("apply_status_time")
    private Date applyStatusTime;
    /**
     * 是否主播 0 ： 否 1：是
     */
    private Integer shower;
    /**
     * 用户等级
     */
    @TableField("user_level")
    private Integer userLevel;
    /**
     * 直播等级
     */
    @TableField("live_level")
    private Integer liveLevel;
    /**
     * 邀请人id
     */
    @TableField("inviter_id")
    private Long inviterId;
    /**
     * 职业
     */
    private String job;
    /**
     * 冻结结束时间
     */
    @TableField("freeze_end_time")
    private Date freezeEndTime;
    /**
     * 冻结开始时间
     */
    @TableField("freeze_start_time")
    private Date freezeStartTime;
    /**
     * 冻结原因
     */
    @TableField("freeze_reason")
    private String freezeReason;
    /**
     * 是否第一次登录 0 否 1是
     */
    @TableField("first_login")
    private Integer firstLogin;
    /**
     * 获赞数
     */
    @TableField("praise_num")
    private Long praiseNum;
    /**
     * 关注数
     */
    @TableField("attention_num")
    private Long attentionNum;
    /**
     * 粉丝数
     */
    @TableField("fans_num")
    private Long fansNum;

    @TableField("apply_agent")
    private Integer applyAgent;
    @TableField("live_state")
    private Integer liveState;

    @TableField("agent_rate")
    private BigDecimal agentRate;

    @TableField("newest_apply_agent_id")
    private Long newestApplyAgentId;
    /**
     * 是否实名 0 ： 否 1：是
     */
    private Integer authed;

    @TableField("is_create_agent")
    private Integer isCreateAgent;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
