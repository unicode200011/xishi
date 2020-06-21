package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.AttentionQuery;
import com.xishi.user.entity.req.AttentionReq;
import com.xishi.user.entity.req.DynamicQuery;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.entity.vo.FansUserVo;
import com.xishi.user.model.pojo.Attention;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 关注 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-28
 */
public interface IAttentionService extends IService<Attention> {

    Resp<List<FansUserVo>> getFansList(AttentionQuery data);

    Resp<List<DynamicVo>> getAttentionDynamicList(DynamicQuery data);

    Resp<Void> submitAttention(AttentionReq data);
}
