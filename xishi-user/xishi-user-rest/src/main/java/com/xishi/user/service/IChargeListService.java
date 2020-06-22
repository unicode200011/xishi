package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.BBPaySubmitReq;
import com.xishi.user.entity.vo.BBPayVo;
import com.xishi.user.entity.vo.ChargeVo;
import com.xishi.user.model.PayResponInfo;
import com.xishi.user.model.pojo.ChargeList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值列表 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface IChargeListService extends IService<ChargeList> {

    Resp<ChargeVo> chargeList(Long userId);

    Resp<Map> submitChargeInfo(BBPayVo data);

    Resp<PayResponInfo> submitPayWay(BBPaySubmitReq data);

    Resp<PayResponInfo> submitBeePayWay(BBPaySubmitReq data);

}
