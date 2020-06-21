package com.xishi.basic.controller;

import com.common.base.model.Resp;
import com.component.cos.CosHelper;
import com.component.cos.CosResult;
import com.xishi.basic.constant.SystemConstants;
import com.xishi.basic.entity.vo.UploadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
@Api(value="文件相关接口",description="文件相关接口")
public class FileController {

    @Autowired
    private CosHelper cosHelper;

    @PostMapping("/upload")
    @ApiOperation("上传文件,参数中bizType为业务类型，交换单投诉中此参值为complain")
    public Resp<UploadVo> upload(@RequestParam(value = "file") MultipartFile file,
                                 @RequestParam(value="userId",required = false, defaultValue = "0") Long userId,
                                 @RequestParam(value="bizType",required = false, defaultValue = "default") String bizType,
                                 @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        String fileName = file.getOriginalFilename().split("\\.")[0];
        log.info("FileController upload start, fileName={},userId={}", fileName, userId);
        String path = SystemConstants.DEFAULT_PATH;
        if(StringUtils.isNotBlank(bizType)) {
            if("complain".equalsIgnoreCase(bizType)) {
                path=SystemConstants.COMPLAIN_PATH;
            }
        }
        CosResult cosResult = cosHelper.uploadResource(path, file);
        if (!cosResult.isSuccess()) {
            return Resp.error("上传失败,请稍后重试");
        }
        UploadVo uploadVo = new UploadVo();
        uploadVo.setImagePath(cosResult.getOrglUrl());
        uploadVo.setSavePath(cosResult.getFullPath());
        uploadVo.setFileName(fileName);
        Resp<UploadVo> resp = new Resp<UploadVo>(uploadVo);
        return resp;
    }
}
