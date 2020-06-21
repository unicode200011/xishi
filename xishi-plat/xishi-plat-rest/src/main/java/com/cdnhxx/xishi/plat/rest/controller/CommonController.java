package com.cdnhxx.xishi.plat.rest.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdnhxx.xishi.plat.entity.req.AdReq;
import com.cdnhxx.xishi.plat.rest.dao.mapper.AdMapper;
import com.cdnhxx.xishi.plat.rest.enums.CommonDataEnum;
import com.cdnhxx.xishi.plat.rest.model.pojo.Ad;
import com.cdnhxx.xishi.plat.rest.model.pojo.CommonData;
import com.cdnhxx.xishi.plat.rest.service.ICommonDataService;
import com.cdnhxx.xishi.plat.entity.req.CommonDataReq;
import com.cdnhxx.xishi.plat.entity.req.CommonDataUpdateReq;
import com.cdnhxx.xishi.plat.entity.vo.*;
import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.common.base.model.Req;
import com.common.base.model.ReqHeader;
import com.common.base.model.Resp;
import com.common.base.util.DateTime;
import com.common.base.util.DateTimeKit;
import com.common.base.util.StrKit;
import com.common.base.util.TransUtil;
import com.component.oss.OssHelper;
import com.component.oss.OssResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 公共数据
 * </p>
 *
 * @author lx
 * @since 2019-08-22
 */
@Slf4j
@Api(value = "公共数据接口", description = "公共数据接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private OssHelper ossHelper;

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private ICommonDataService commonDataService;


    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    @NeedLogin
    public Resp<OssResult> ueUpload(@RequestParam(value = "file") MultipartFile file,
                                    @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        return Resp.successData(ossHelper.uploadObject2OSS(file, fileType));
    }


    @ApiOperation("批量图片上传")
    @PostMapping("/multiUpload")
    @NeedLogin
    public Resp<List<OssResult>> multiUpload(@RequestParam(value = "file") MultipartFile[] file,
                                             @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        List<OssResult> files = new ArrayList<>();
        if (file == null || file.length <= 0) {
            return Resp.error("请上传文件");
        }
        for (MultipartFile multipartFile : file) {
            files.add(ossHelper.uploadObject2OSS(multipartFile, fileType));
        }
        return Resp.successData(files);
    }


    @PostMapping("/commonData")
    @ApiOperation("获取公共信息")
    public Resp<CommonDataVo> commonData(@RequestBody Req<CommonDataReq> req) {
        CommonDataReq dataReq = req.getData();
        Integer commonNum = (dataReq == null || dataReq.getCommonNum() == null) ? null : dataReq.getCommonNum();
        if (commonNum == null) {
            return Resp.error("参数错误");
        }
        CommonData byId = commonDataService.getById(commonNum);
        if(byId == null){
            return Resp.error("参数错误");
        }
        CommonDataVo dataVo = new CommonDataVo();
        dataVo.setName(byId.getDescription());
        dataVo.setContent(byId.getValue());
        Resp<CommonDataVo> resp = new Resp<CommonDataVo>(dataVo);
        return resp;
    }

    @PostMapping("/commonDataSensitive")
    @ApiOperation("获取公共信息-敏感数据获取-APP勿调用")
    @InnerInvoke
    public Resp<CommonDataVo> commonDataSensitive(@RequestBody Req<CommonDataReq> req) {
        CommonDataReq dataReq = req.getData();
        Integer commonNum = (dataReq == null || dataReq.getCommonNum() == null) ? null : dataReq.getCommonNum();
        CommonDataEnum dataEnum = CommonDataEnum.getDateEnumByNum(commonNum);
        if (dataEnum == null) {
            return Resp.error("参数错误");
        }
        String commonData = commonDataService.queryCommonDataFromCache(dataEnum.getKey());
        CommonDataVo dataVo = new CommonDataVo();
        dataVo.setName(dataEnum.getName());
        dataVo.setContent(commonData);
        Resp<CommonDataVo> resp = new Resp<>(dataVo);
        return resp;
    }

    @PostMapping("/commonDataUpdate")
    @ApiOperation("公共参数更新-APP勿调用")
    @InnerInvoke
    public Resp<Void> commonDataUpdate(@RequestBody Req<CommonDataUpdateReq> req) {
        CommonDataUpdateReq data = req.getData();
        CommonDataEnum dataEnum = CommonDataEnum.getDateEnumByNum(data.getCommonNum());
        CommonData commonData = commonDataService.queryByKey(dataEnum.getKey());
        commonData.setValue(data.getValue());
        commonData.setUpdateTime(new Date());
        if (commonDataService.updateById(commonData)) {
            commonDataService.commonDataToCache(dataEnum.getKey(), commonData.getValue());
        }
        return Resp.success();
    }


    @PostMapping("/ads")
    @ApiOperation("0：首页顶部轮播 1：首页固定广告位  6：启动页  7：引导页")
    public Resp<List<AdDetailVo>> ads(@RequestBody Req<AdReq> req) {
        AdReq location = req.getData();
        QueryWrapper<Ad> qw = new QueryWrapper<Ad>()
                .eq("remark", location.getType())
                .eq("state", 0)
                .orderByDesc("id");
        if (location.getType() == 1) {
            qw.last(" LIMIT 4 ");
        } else if(location.getType() > 1){
            qw.last(" LIMIT 1 ");
        }
        List<Ad> ads = adMapper.selectList(qw);
        List<AdDetailVo> adDetailVo = TransUtil.transList(ads, AdDetailVo.class);
        return Resp.successData(adDetailVo);
    }
    @PostMapping("/liveAds")
    @ApiOperation("获取直播页广告 remark值 2：直播页左方1 3：直播页左方2 4：直播页右方1 5：直播页右方2")
    public Resp<List<AdDetailVo>> liveAds(@RequestBody Req<Void> req) {
        QueryWrapper<Ad> qw = new QueryWrapper<Ad>()
                .in("remark", Arrays.asList(2,3,4,5))
                .eq("state", 0)
                .orderByDesc("id");
        List<Ad> ads = adMapper.selectList(qw);
        List<AdDetailVo> adDetailVo = TransUtil.transList(ads, AdDetailVo.class);
        return Resp.successData(adDetailVo);
    }

    @PostMapping("/adDetail")
    @ApiOperation("广告详情,参数广告ID")
    public Resp<AdDetailVo> adDetail(@RequestBody Req<AdReq> req) {
        AdReq adId = req.getData();
        Ad ad = adMapper.selectById(adId.getId());
        Integer state = ad.getState();
        if(state != 0){
            return Resp.error("广告已被禁用");
        }
        if (ad != null) {
            ad.setClickTimes(ad.getClickTimes() + 1);
            ad.setUpdateTime(new Date());
            adMapper.updateById(ad);
            AdDetailVo adDetailVo = TransUtil.transEntity(ad, AdDetailVo.class);
            adDetailVo.setTime(DateTimeKit.formatDateTime(ad.getCreateTime()));
            return Resp.successData(adDetailVo);
        }
        return Resp.error("广告不存在");
    }

    @ApiOperation("版本检查")
    @PostMapping("/versionCheck")
    public Resp<VersionInfoVo> versionCheck(@RequestBody Req<Void> req) {
        ReqHeader header = req.getHeader();
        //安卓 android; iOS:iOS
        String deviceOs = header.getDeviceOs();
        if (StrKit.isEmpty(deviceOs)) {
            return Resp.error("设备信息错误");
        }

        String minV = "";
        String maxV = "";
        String desc = "";
        String downUrl = "";

        if ("iOS".equals(deviceOs)) {
            minV = commonDataService.queryCommonDataFromCache(CommonDataEnum.IOS_MIN.getKey());
            maxV = commonDataService.queryCommonDataFromCache(CommonDataEnum.IOS_MAX.getKey());
            desc = commonDataService.queryCommonDataFromCache(CommonDataEnum.IOS_UPDATE_DESC.getKey());
            downUrl = commonDataService.queryCommonDataFromCache(CommonDataEnum.IOS_DOWN_URL.getKey());
        }
        if ("Android".equals(deviceOs)) {
            minV = commonDataService.queryCommonDataFromCache(CommonDataEnum.AND_MIN.getKey());
            maxV = commonDataService.queryCommonDataFromCache(CommonDataEnum.AND_MAX.getKey());
            desc = commonDataService.queryCommonDataFromCache(CommonDataEnum.AND_UPDATE_DESC.getKey());
            downUrl = commonDataService.queryCommonDataFromCache(CommonDataEnum.AND_DOWN_URL.getKey());
        }
        return Resp.successData(new VersionInfoVo(minV, maxV, desc, downUrl));
    }

    @ApiOperation("安卓与Ios下载地址")
    @PostMapping("/appDownUrl")
    public Resp<AppDownInfoVo> appDownUrl(@RequestBody Req<Void> req) {
        String iosDownUrl = commonDataService.queryCommonDataFromCache(CommonDataEnum.IOS_DOWN_URL.getKey());
        String andDownUrl = commonDataService.queryCommonDataFromCache(CommonDataEnum.AND_DOWN_URL.getKey());
        return Resp.successData(new AppDownInfoVo(iosDownUrl, andDownUrl));
    }

}
