package com.cdnhxx.xishi.plat.rest.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 举报列表
 * </p>
 *
 * @author LX
 * @since 2019-12-27
 */
@Data
@TableName("eb_complain")
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 举报用户id
     */
    private Long userId;

    /**
     * 被举报用户id
     */
    private Long linkUid;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 举报者用户名
     */
    private String userName;

    /**
     * 被举报者用户名
     */
    private String linkUname;

    /**
     * 举报时间
     */
    private Date createTime;


}
