package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import com.xishi.user.entity.vo.ChargeListVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "ChargeRecordReq", description = "记录")
public class ChargeRecordReq  extends BaseQuery {
}
