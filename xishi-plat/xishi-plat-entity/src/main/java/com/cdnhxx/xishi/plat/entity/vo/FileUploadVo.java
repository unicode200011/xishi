package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传
 *
 * @author: lx
 */
@ApiModel(value = "fileUploadVo", description = "文件上传返回结果")
@Data
public class FileUploadVo {

    @ApiModelProperty("文件访问地址,回传后端")
    private String filePath;
    @ApiModelProperty("文件相对路径,暂不用")
    private String savePath;
    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("文件大小")
    private Long size;


    public FileUploadVo() {
    }

    public FileUploadVo(String filePath, String savePath, String fileName, Long size) {
        this.filePath = filePath;
        this.savePath = savePath;
        this.fileName = fileName;
        this.size = size;
    }
}
