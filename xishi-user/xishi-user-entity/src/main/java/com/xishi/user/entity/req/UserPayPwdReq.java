package com.xishi.user.entity.req;

import com.xishi.user.entity.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserPayPwdReq", description = "用户支付密码")
public class UserPayPwdReq {

    @ApiModelProperty("支付密码")
    private String pwd;

}
