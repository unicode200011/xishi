package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.BBPayReq;
import com.xishi.user.entity.vo.BBPayWayVo;
import com.xishi.user.model.pojo.PayAndType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.model.pojo.PayCallBackDataInfo;

import java.util.List;

/**
 * <p>
 * 支付方式和支付类型关联表 服务类
 * </p>
 *
 * @author LX
 * @since 2020-02-14
 */
public interface IPayAndTypeService extends IService<PayAndType> {

    Resp<List<BBPayWayVo>> getPayWayList(BBPayReq data);

    Resp bbPayCallBack(PayCallBackDataInfo payCallBackDataInfo);

    Resp beePayCallBack(PayCallBackDataInfo payCallBackDataInfo);

}
