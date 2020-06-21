package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "WithdrawalRecordReq", description = "提现记录")
public class WithdrawalRecordReq extends BaseQuery {
}
