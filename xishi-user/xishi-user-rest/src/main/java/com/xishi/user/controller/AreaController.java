package com.xishi.user.controller;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.xishi.user.model.pojo.Cities;
import com.xishi.user.model.pojo.Provinces;
import com.xishi.user.entity.vo.CityVo;
import com.xishi.user.entity.vo.ProvinceVo;
import com.xishi.user.service.ICityService;
import com.xishi.user.service.IProvinceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/area")
@Slf4j
@Api(value="地域接口",description="地域接口")
public class AreaController {

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private ICityService cityService;

    @RequestMapping(value="/queryCityByProvince",method = RequestMethod.POST)
    @ApiOperation("查询某省的城市列表,data传参为省份ID")
    public Resp<List<CityVo>> queryCityByProvince(@RequestBody Req<String> req) {
        String provinceId=req.getData();
        if(StringUtils.isBlank(provinceId)) {
            return new Resp<List<CityVo>>(Collections.emptyList());
        }
        List<Cities> list = cityService.queryCityByProvince(provinceId);
        List<CityVo> rtList = TransUtil.transList(list,CityVo.class);
        Resp<List<CityVo>> resp = new Resp<List<CityVo>>(rtList);
        return resp;

    }

    @RequestMapping(value="/queryProvinceList",method = RequestMethod.POST)
    @ApiOperation("查询省份列表")
    public Resp<List<ProvinceVo>> queryProvinceList(@RequestBody Req<Void> req) {
        List<Provinces> list = provinceService.list();
        List<ProvinceVo> rtList = TransUtil.transList(list,ProvinceVo.class);
        Resp<List<ProvinceVo>> resp = new Resp<List<ProvinceVo>>(rtList);
        return resp;
    }
}
