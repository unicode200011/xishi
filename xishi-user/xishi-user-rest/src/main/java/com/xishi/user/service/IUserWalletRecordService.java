package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.BankUserReq;
import com.xishi.user.entity.req.ChargeRecordReq;
import com.xishi.user.entity.req.LiveGiftListQuery;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.UserWalletRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户钱包记录 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface IUserWalletRecordService extends IService<UserWalletRecord> {

    Resp<List<UserWalletGiftRecordVo>> getUserGiftRecord(ChargeRecordReq data);

    Resp<List<UserWalletRecordVo>> getUserWalletRecord(ChargeRecordReq data);

    Resp<MoneyDetailVo> walletDetail(ChargeRecordReq data);

    Resp<List<LiveGiftListVo>> getLiveGiftList(LiveGiftListQuery query);

    Resp<List<BankAccountTypeVo>> getBankAccountType(Long userId);

    Resp<Void> addBankAccountType(BankUserReq data);

    Resp<List<BankUserVo>> getUserBankAccountList(Long userId);

    Resp<Void> deleteUserBankAccount(BankUserVo data);

    Resp<List<LiveGiftRankingVo>> getLiveGiftRanking(Long data);
}
