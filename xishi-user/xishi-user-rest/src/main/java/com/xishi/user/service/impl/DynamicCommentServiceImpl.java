package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.req.DynamicCommentBackReq;
import com.xishi.user.entity.req.DynamicCommentQuery;
import com.xishi.user.entity.req.DynamicCommentReq;
import com.xishi.user.entity.vo.DynamicCommentBackVo;
import com.xishi.user.entity.vo.DynamicCommentVo;
import com.xishi.user.model.pojo.Dynamic;
import com.xishi.user.model.pojo.DynamicComment;
import com.xishi.user.dao.mapper.DynamicCommentMapper;
import com.xishi.user.model.pojo.User;
import com.xishi.user.service.IDynamicCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IDynamicService;
import com.xishi.user.service.IKeywordService;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 动态评论表 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-18
 */
@Service
public class DynamicCommentServiceImpl extends ServiceImpl<DynamicCommentMapper, DynamicComment> implements IDynamicCommentService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDynamicService dynamicService;
    @Autowired
    private IKeywordService keywordService;


    @Override
    public Resp<DynamicCommentVo> submitDynamicComment(DynamicCommentReq data) {
        //屏蔽关键字
        String content = keywordService.replaceWord(data.getContent());
        data.setContent(content);
        DynamicComment dynamicComment = TransUtil.transEntity(data, DynamicComment.class);
        User byId = userService.getById(dynamicComment.getUserId());
        dynamicComment.setUserName(byId==null?"":byId.getName());
        this.save(dynamicComment);
        //组装评论内容
        DynamicCommentVo dynamicCommentVo = TransUtil.transEntity(dynamicComment, DynamicCommentVo.class);
        dynamicCommentVo.setAvatar(byId.getAvatar());
        //更新动态的评论数量 +1
        dynamicService.changeCommentNum(dynamicComment.getDynamicId(),0);
        return Resp.successData(dynamicCommentVo);
    }

    @Override
    public Resp<DynamicCommentBackVo> submitDynamicCommentBack(DynamicCommentBackReq data) {
        //屏蔽关键字
        String content = keywordService.replaceWord(data.getContent());
        data.setContent(content);

        DynamicComment dynamicComment = TransUtil.transEntity(data, DynamicComment.class);
        User byId = userService.getById(dynamicComment.getUserId());
        User backUser = userService.getById(data.getBackUserId());
        dynamicComment.setUserName(byId==null?"":byId.getName());
        dynamicComment.setRemark("回复"+backUser.getName());
        this.save(dynamicComment);
        //组装回复内容
        DynamicCommentBackVo dynamicCommentBackVo = TransUtil.transEntity(dynamicComment, DynamicCommentBackVo.class);
        dynamicCommentBackVo.setAvatar(byId.getAvatar());
        //更新动态的评论数量 +1
        dynamicService.changeCommentNum(dynamicComment.getDynamicId(),0);
        return Resp.successData(dynamicCommentBackVo);
    }

    @Override
    public Resp<List<DynamicCommentVo>> DynamicCommentList(DynamicCommentQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<DynamicComment> queryWrapper = new QueryWrapper();
        queryWrapper.eq("dynamic_id",data.getDynamicId());
        queryWrapper.eq("parent_id",0);
        queryWrapper.eq("state",0);
        queryWrapper.orderByDesc("create_time");
        List<DynamicComment> list = this.list(queryWrapper);
        //查询该动态的所有第一级评论
        List<DynamicCommentVo> dynamicCommentVos = TransUtil.transList(list, DynamicCommentVo.class);
        for (DynamicCommentVo dynamicCommentVo : dynamicCommentVos) {
            //评论下的回复
            List<DynamicCommentBackVo> dynamicCommentBackVos = this.getCommentBackByCommentId(dynamicCommentVo.getId(),1,5);
            for (DynamicCommentBackVo dynamicCommentBackVo : dynamicCommentBackVos) {
                dynamicCommentBackVo.setCreateTimeStr(DateUtils.parsePastTime(dynamicCommentBackVo.getCreateTime()));
            }
            dynamicCommentVo.setCommentBackVos(dynamicCommentBackVos);
        }
        return Resp.successData(dynamicCommentVos);
    }

    @Override
    public Resp<List<DynamicCommentBackVo>> dynamicCommentBackList(DynamicCommentQuery data) {
        List<DynamicCommentBackVo> commentBackByCommentId = this.getCommentBackByCommentId(data.getDynamicId(), data.getPage(), data.getRows());
        return Resp.successData(commentBackByCommentId);
    }

    /**
     * 查询评论下的回复
     */
    public List<DynamicCommentBackVo> getCommentBackByCommentId(Long commentId,Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        QueryWrapper<DynamicComment> queryWrapperBack = new QueryWrapper();
        queryWrapperBack.eq("parent_id",commentId);
        queryWrapperBack.eq("state",0);
        queryWrapperBack.orderByDesc("create_time");
        List<DynamicComment> backList = this.list(queryWrapperBack);
        //查询该评论的回复列表
        List<DynamicCommentBackVo> dynamicCommentBackVos = TransUtil.transList(backList, DynamicCommentBackVo.class);
        for (DynamicCommentBackVo dynamicCommentBackVo : dynamicCommentBackVos) {
            User byId = userService.getById(dynamicCommentBackVo.getUserId());
            dynamicCommentBackVo.setUserName(byId==null?"":byId.getName());
            dynamicCommentBackVo.setAvatar(byId==null?"":byId.getAvatar());
        }
        return dynamicCommentBackVos;
    }


}
