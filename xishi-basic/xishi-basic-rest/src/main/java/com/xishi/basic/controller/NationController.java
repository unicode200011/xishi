package com.xishi.basic.controller;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.xishi.basic.model.pojo.Nation;
import com.xishi.basic.entity.vo.NationVo;
import com.xishi.basic.service.INationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nation")
@Api(value="民族接口",description="民族接口")
public class NationController {

    @Autowired
    private INationService nationService;

    @PostMapping("/list")
    @ApiOperation("民族列表")
    public Resp<List<NationVo>> list(@RequestBody Req<Void> req) {
        List<Nation> list = nationService.queryListFromCache();
        List<NationVo> rtList = TransUtil.transList(list,NationVo.class);
        Resp<List<NationVo>> resp = new Resp<List<NationVo>>(rtList);
        return resp;
    }
}
