package com.cdnhxx.xishi.plat.entity.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * @author: lx
 */
@ApiModel(value = "suggestionReq", description = "意见反馈")
@Data
public class SuggestionReq {

    private Long typeId;

    @NotEmpty(message = "请输入意见或者建议")
    @Length(max = 200, message = "字数超过限制")
    private String content;
}
