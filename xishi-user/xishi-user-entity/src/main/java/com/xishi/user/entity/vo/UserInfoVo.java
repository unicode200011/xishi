package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "userInfoVo", description = "用户个人资料")
public class UserInfoVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("西施号")
    private String xishiNum;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("性别: 0:男 1:女  2:保密")
    private Integer gender;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("地址,省-市-区")
    private String address;

    @ApiModelProperty("个人简介")
    private String intro;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区县")
    private String county;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("用户总状态 0:正常 1:冻结  2:删除")
    private Integer state;

    @ApiModelProperty("推荐人id")
    private Long inviterId;

    @ApiModelProperty("推荐人西施号")
    private String inviterXishiNum;

    @ApiModelProperty("冻结结束时间")
    private Date freezeEndTime;

    @ApiModelProperty("冻结开始时间")
    private Date freezeStartTime;

    @ApiModelProperty("冻结原因")
    private String freezeReason;

    @ApiModelProperty("职业")
    private String job;

    @ApiModelProperty("主播认证申请状态:1:已申请2:未申请3:拒绝4:通过")
    private Integer applyStatus;

    @ApiModelProperty("是否主播 0 ： 否 1：是")
    private Integer shower;

    @ApiModelProperty("主播直播等级")
    private Integer liveLevel;

    @ApiModelProperty("是否设置支付密码 0 ： 否 1：是")
    private Integer hasPayPwd;

    @ApiModelProperty("是否实名 0 ： 否 1：是")
    private Integer authed;

    @ApiModelProperty("用户等级")
    private Integer userLevel;

    @ApiModelProperty("用户等级图片")
    private String userLevelPic;

    @ApiModelProperty("用户等级颜色 rgb")
    private String userLevelColor;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("wx")
    private String wx;

    @ApiModelProperty("所属家族id")
    private Long belongAgent;

    @ApiModelProperty("所属家族名字")
    private String belongAgentName;

    @ApiModelProperty("获赞数")
    private Long praiseNum;

    @ApiModelProperty("关注数")
    private Long attentionNum;

    @ApiModelProperty("粉丝数")
    private Long fansNum;

    @ApiModelProperty("是否申请创建家族0：否 1：通过 2：驳回 3：申请中")
    private Integer applyAgent;

    @ApiModelProperty("是否正在直播0：否 1：是")
    private Integer liveState;

    @ApiModelProperty("是否关注 0：否 1：是")
    private Integer attention;

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("房间密码 密码模式有效")
    private String livePwd;

    @ApiModelProperty("西施币 收费模式有效 ")
    private String livePrice;

    @ApiModelProperty("直播Id")
    private Long liveId;

    @ApiModelProperty("直播流名")
    private String streamName;

}
