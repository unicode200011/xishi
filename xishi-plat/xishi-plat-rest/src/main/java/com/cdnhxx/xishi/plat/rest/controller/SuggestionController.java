package com.cdnhxx.xishi.plat.rest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdnhxx.xishi.plat.rest.model.pojo.Suggestion;
import com.cdnhxx.xishi.plat.rest.model.pojo.SuggestionType;
import com.cdnhxx.xishi.plat.rest.service.ISuggestionService;
import com.cdnhxx.xishi.plat.rest.service.ISuggestionTypeService;
import com.cdnhxx.xishi.plat.entity.req.SuggestionReq;
import com.cdnhxx.xishi.plat.entity.vo.SuggestionTypeVo;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 *
 * @author: lx
 */
@Slf4j
@Api(value = "意见反馈", description = "意见反馈")
@RestController
@RequestMapping("/suggestion")
public class SuggestionController {

    @Autowired
    private ISuggestionService suggestionService;

    @Autowired
    private ISuggestionTypeService suggestionTypeService;


    @ApiOperation("意见反馈类型")
    @PostMapping("/suggestionType")
    public Resp<List<SuggestionTypeVo>> suggestionType(@RequestBody Req<Void> req) {
        List<SuggestionType> list = suggestionTypeService.list(new QueryWrapper<SuggestionType>()
                .eq("state", "0")
                .orderByDesc("id")
        );
        return Resp.successData(TransUtil.transList(list, SuggestionTypeVo.class));
    }

    @ApiOperation("意见反馈")
    @NeedLogin
    @PostMapping("/suggestion")
    public Resp<Void> suggestion(@RequestBody @Valid Req<SuggestionReq> req) {
        Long userId = LoginContext.getLoginUser().getUserId();
        SuggestionReq data = req.getData();
        log.info("用户反馈[{}]",data.getContent());
        Long typeId = data.getTypeId();
        SuggestionType suggestionType = suggestionTypeService.getById(typeId);
        if (suggestionType == null || suggestionType.getState() != 0) {
            return Resp.error("类型不存在");
        }
        Suggestion suggestion = new Suggestion();
        suggestion.setTypeId(typeId);
        suggestion.setUserId(userId);
        suggestion.setState(0);
        suggestion.setContent(data.getContent());
        suggestion.setCreateTime(new Date());
        suggestion.setUpdateTime(new Date());
        suggestionService.save(suggestion);
        return Resp.success("感谢您的反馈!");
    }
}
