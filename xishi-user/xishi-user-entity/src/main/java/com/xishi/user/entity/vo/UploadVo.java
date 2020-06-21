package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "uploadVo", description = "文件上传结果")
public class UploadVo {

    @ApiModelProperty("文件访问全路径")
    private String imagePath;

    @ApiModelProperty("文件path")
    private String savePath;

    @ApiModelProperty("文件名")
    private String fileName;

}
