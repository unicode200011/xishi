package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "DynamicCommentQuery", description = "动态评论列表")
public class DynamicCommentQuery extends BaseQuery {

    @ApiModelProperty("查询动态评论传入动态id / 查询评论下回复列表传入 评论id")
    private Long dynamicId;
}
