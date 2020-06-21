package com.cdnhxx.xishi.plat.rest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdnhxx.xishi.plat.entity.req.ComplainReq;
import com.cdnhxx.xishi.plat.entity.req.SuggestionReq;
import com.cdnhxx.xishi.plat.entity.vo.ComplainReasonVo;
import com.cdnhxx.xishi.plat.entity.vo.SuggestionTypeVo;
import com.cdnhxx.xishi.plat.rest.model.pojo.Complain;
import com.cdnhxx.xishi.plat.rest.model.pojo.ComplainType;
import com.cdnhxx.xishi.plat.rest.model.pojo.Suggestion;
import com.cdnhxx.xishi.plat.rest.model.pojo.SuggestionType;
import com.cdnhxx.xishi.plat.rest.service.IComplainService;
import com.cdnhxx.xishi.plat.rest.service.IComplainTypeService;
import com.cdnhxx.xishi.plat.rest.service.ISuggestionService;
import com.cdnhxx.xishi.plat.rest.service.ISuggestionTypeService;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 *
 * @author: lx
 */
@Slf4j
@Api(value = "举报相关", description = "举报相关")
@RestController
@RequestMapping("/complain")
public class ComplainController {

    @Autowired
    private IComplainService complainService;

    @Autowired
    private IComplainTypeService complainTypeService;


    @ApiOperation("举报理由")
    @PostMapping("/complainReason")
    public Resp<List<ComplainReasonVo>> complainReason(@RequestBody Req<Void> req) {
        List<ComplainType> list = complainTypeService.list(new QueryWrapper<ComplainType>()
                .orderByDesc("id")
        );
        return Resp.successData(TransUtil.transList(list, ComplainReasonVo.class));
    }

    @ApiOperation("提交举报")
    @NeedLogin
    @PostMapping("/submitComplain")
    public Resp<Void> submitComplain(@RequestBody Req<ComplainReq> req) {
        Long userId = LoginContext.getLoginUser().getUserId();
        ComplainReq data = req.getData();
        Complain complain = TransUtil.transEntity(data, Complain.class);
        complain.setUserId(userId);
        complainService.save(complain);
        return Resp.success("举报成功");
    }
}
