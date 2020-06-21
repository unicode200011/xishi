package com.xishi.basic.controller;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.basic.entity.req.CommonDataReq;
import com.xishi.basic.entity.enums.CommonDataEnum;
import com.xishi.basic.entity.vo.CommonDataVo;
import com.xishi.basic.service.ICommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
@Slf4j
@Api(value = "公共信息接口", description = "公共信息接口")
public class CommonController {

    @Autowired
    private ICommonService commonService;

    @PostMapping("/commonData")
    @ApiOperation("获取公共信息")
    public Resp<CommonDataVo> commonData(@RequestBody Req<CommonDataReq> req) {
        CommonDataReq dataReq = req.getData();
        Integer commonNum = (dataReq == null || dataReq.getCommonNum() == null) ? null : dataReq.getCommonNum();
        if (commonNum == null) {
            return Resp.error("参数错误");
        }
        CommonDataEnum dataEnum = CommonDataEnum.getDateEnumByNum(commonNum);
        if (dataEnum == null) {
            return Resp.error("参数错误");
        }
        String dataKey = dataEnum.getKey();

        String commonData = "";
        switch (dataEnum) {
            //用户协议
            case USER_XY:
                commonData = commonService.queryCommonDataFromCache(dataKey);
                break;
            //联系我们
            case CONTACT_US:
                commonData = commonService.queryCommonDataFromCache(dataKey);
                break;
            //看值说明
            case KANVALUE_NOTE:
                commonData = commonService.queryCommonDataFromCache(dataKey);
                break;
            default:
                commonData = "";
                break;
        }
        CommonDataVo dataVo = new CommonDataVo();
        dataVo.setName(dataEnum.getName());
        dataVo.setContent(commonData);
        Resp<CommonDataVo> resp = new Resp<CommonDataVo>(dataVo);
        return resp;
    }

    @PostMapping("/cleanDataCache")
    @ApiOperation("清除公共信息缓存")
    public Resp<Void> cleanDataCache(@RequestBody Req<CommonDataReq> req) {
        CommonDataReq dataReq = req.getData();
        Integer commonNum = (dataReq == null || dataReq.getCommonNum() == null) ? null : dataReq.getCommonNum();
        CommonDataEnum dataEnum = null;
        if (commonNum != null) {
            dataEnum = CommonDataEnum.getDateEnumByNum(commonNum);
        }
        if (dataEnum == null) {
            return Resp.error("参数错误");
        }
        String dataKey = dataEnum.getKey();
        commonService.cleanDataCache(dataKey);
        return Resp.success();
    }
}
