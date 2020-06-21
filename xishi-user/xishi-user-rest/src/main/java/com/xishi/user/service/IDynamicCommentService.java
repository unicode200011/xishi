package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.DynamicCommentBackReq;
import com.xishi.user.entity.req.DynamicCommentQuery;
import com.xishi.user.entity.req.DynamicCommentReq;
import com.xishi.user.entity.vo.DynamicCommentBackVo;
import com.xishi.user.entity.vo.DynamicCommentVo;
import com.xishi.user.model.pojo.DynamicComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动态评论表 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-18
 */
public interface IDynamicCommentService extends IService<DynamicComment> {

    Resp<DynamicCommentVo> submitDynamicComment(DynamicCommentReq data);

    Resp<DynamicCommentBackVo> submitDynamicCommentBack(DynamicCommentBackReq data);

    Resp<List<DynamicCommentVo>> DynamicCommentList(DynamicCommentQuery data);

    Resp<List<DynamicCommentBackVo>> dynamicCommentBackList(DynamicCommentQuery data);
}
