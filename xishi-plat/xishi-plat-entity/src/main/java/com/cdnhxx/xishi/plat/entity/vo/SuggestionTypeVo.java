package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lx
 */
@ApiModel(value = "suggestionTypeVo", description = "意见反馈类型")
@Data
public class SuggestionTypeVo {

    private Long id;

    private String name;
}
