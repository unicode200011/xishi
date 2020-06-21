package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lx
 */
@ApiModel(value = "ComplainReasonVo", description = "举报理由")
@Data
public class ComplainReasonVo {
    private String reason;
    private Integer id;
}
