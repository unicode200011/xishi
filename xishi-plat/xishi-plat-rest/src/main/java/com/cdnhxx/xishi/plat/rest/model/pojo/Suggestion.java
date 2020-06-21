package com.cdnhxx.xishi.plat.rest.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 意见反馈类型
 * </p>
 *
 * @author lx
 * @since 2019-10-15
 */
@Data
@TableName("eb_suggestion")
public class Suggestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 反馈用户ID
     */
    private Long userId;

    /**
     * 类型ID
     */
    private Long typeId;

    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 状态 0 待处理 1 已处理 2 删除
     */
    private Integer state;

    /**
     * 回复内容1
     */
    private String firstRepContent;

    /**
     * 回复内容2
     */
    private String secondRepContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


}
