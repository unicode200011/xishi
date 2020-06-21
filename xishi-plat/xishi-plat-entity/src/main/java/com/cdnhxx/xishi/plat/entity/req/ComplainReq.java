package com.cdnhxx.xishi.plat.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * @author: lx
 */
@ApiModel(value = "ComplainReq", description = "举报")
@Data
public class ComplainReq {

    private Long userId;

    @ApiModelProperty("被举报用户id")
    private Long linkUid;

    @ApiModelProperty("举报理由")
    private String reason;

    @ApiModelProperty("举报者用户名")
    private String userName;

    @ApiModelProperty("被举报者用户名")
    private String linkUname;
}
