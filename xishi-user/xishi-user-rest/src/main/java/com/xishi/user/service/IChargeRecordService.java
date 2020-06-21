package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.ChargeRecordReq;
import com.xishi.user.entity.vo.ChargeRecordVo;
import com.xishi.user.model.pojo.ChargeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface IChargeRecordService extends IService<ChargeRecord> {

    Resp<List<ChargeRecordVo>> getUserChargeRecord(ChargeRecordReq data);
}
