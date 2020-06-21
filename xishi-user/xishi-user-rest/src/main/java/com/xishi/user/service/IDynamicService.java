package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.DynamicQuery;
import com.xishi.user.entity.req.DynamicReq;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.model.pojo.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动态列表 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
public interface IDynamicService extends IService<Dynamic> {

    Resp<Void> sendDynamic(DynamicReq data);

    Resp<Void> deleteDynamic(DynamicReq data);

    Resp<Void> praiseDynamic(DynamicReq data);

    Resp<List<DynamicVo>> getDynamics(DynamicQuery data);
    void genDynamicVo(List<DynamicVo> dynamicVos,Long userId);

    void changeCommentNum(Long dynamicId,Integer type);
}
