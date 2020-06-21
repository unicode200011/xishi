package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.WithdrawalRecordReq;
import com.xishi.user.entity.req.WithdrawalSubmitReq;
import com.xishi.user.entity.vo.WithdrawalVo;
import com.xishi.user.model.pojo.Withdrawal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提现 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-08
 */
public interface IWithdrawalService extends IService<Withdrawal> {

    Resp<List<WithdrawalVo>> withdrawalRecord(WithdrawalRecordReq data);

    Resp<Void> withdrawalSubmit(WithdrawalSubmitReq data);
}
