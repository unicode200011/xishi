package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付通道
 * </p>
 *
 * @author LX
 * @since 2020-02-14
 */
@Data
@TableName("eb_pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付名
     */
    private String name;

    /**
     * 创建订单API 主用地址
     */
    private String createMusterUrl;

    /**
     * 创建订单API 备用地址
     */
    private String createClusterUrl;

    /**
     * 查询订单备用地址
     */
    private String queryClusterUrl;

    /**
     * 查询订单主用地址
     */
    private String queryMusterUrl;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 商户标识
     */
    private String merchantId;

    /**
     * API 密钥
     */
    private String apiKey;

    /**
     * 加密类型
     */
    private String encodeType;


}
