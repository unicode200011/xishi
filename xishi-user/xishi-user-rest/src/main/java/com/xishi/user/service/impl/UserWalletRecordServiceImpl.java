package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.req.BankUserReq;
import com.xishi.user.entity.req.ChargeRecordReq;
import com.xishi.user.entity.req.LiveGiftListQuery;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.UserWalletRecordMapper;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 用户钱包记录 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Slf4j
@Service
public class UserWalletRecordServiceImpl extends ServiceImpl<UserWalletRecordMapper, UserWalletRecord> implements IUserWalletRecordService {

    @Autowired
    IUserService userService;
    @Autowired
    IUserWalletService userWalletService;
    @Autowired
    IBankAccountService bankAccountService;
    @Autowired
    IBankUserService bankUserService;
    @Autowired
    IWatchRecordService watchRecordService;



    @Override
    public Resp<List<UserWalletGiftRecordVo>> getUserGiftRecord(ChargeRecordReq data) {
        System.out.println(1);
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<UserWalletRecord> wrapper = new QueryWrapper<UserWalletRecord>();
        wrapper.eq("user_id", data.getUserId());
        wrapper.eq("use_type", 0);//收入
//        wrapper.in("type", Arrays.asList(2,3,4));
        wrapper.in("type", 2,3);
        wrapper.orderByDesc("id");
        List<UserWalletRecord> userWalletRecords = this.list(wrapper);
        UserWallet userWallet = userWalletService.findWalletByUserId(data.getUserId());
        List<UserWalletGiftRecordVo> userWalletRecordVos = TransUtil.transList(userWalletRecords, UserWalletGiftRecordVo.class);

        for (UserWalletGiftRecordVo userWalletRecordVo : userWalletRecordVos) {
            User byId = userService.getById(userWalletRecordVo.getLinkUid());
            userWalletRecordVo.setUserName(byId==null?"":byId.getName());
            userWalletRecordVo.setWalletAmount(userWallet.getGiftAmount());
        }
        Resp<List<UserWalletGiftRecordVo>> resp = new Resp<List<UserWalletGiftRecordVo>>(userWalletRecordVos);
        return resp;
    }

    @Override
    public Resp<List<UserWalletRecordVo>> getUserWalletRecord(ChargeRecordReq data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<UserWalletRecord> wrapper = new QueryWrapper<UserWalletRecord>();
        wrapper.eq("user_id", data.getUserId());
        wrapper.eq("use_type", 1);//支出
        wrapper.orderByDesc("id");
        List<UserWalletRecord> userWalletRecords = this.list(wrapper);
        List<UserWalletRecordVo> userWalletRecordVos = TransUtil.transList(userWalletRecords, UserWalletRecordVo.class);
        Resp<List<UserWalletRecordVo>> resp = new Resp<List<UserWalletRecordVo>>(userWalletRecordVos);
        return resp;
    }

    @Override
    public Resp<MoneyDetailVo> walletDetail(ChargeRecordReq data) {
        UserWallet userWallet = userWalletService.findWalletByUserId(data.getUserId());
        MoneyDetailVo moneyDetailVo = new MoneyDetailVo();
        moneyDetailVo.setGiftAmount(userWallet.getGiftAmount());
        moneyDetailVo.setWalletAmount(userWallet.getGbMoeny());
        moneyDetailVo.setWithdrawalAmount(userWallet.getGiftAmount());
        String rate = userWalletService.findCommon();
        moneyDetailVo.setRate(Integer.valueOf(rate));
        return new Resp<>(moneyDetailVo);
    }

    @Override
    public Resp<List<LiveGiftListVo>> getLiveGiftList(LiveGiftListQuery query) {
        PageHelper.startPage(query.getPage(),query.getRows());
        List<LiveGiftListVo> liveGiftListVos = baseMapper.getLiveGiftList(query.getLiveRecordId());
        return Resp.successData(liveGiftListVos);
    }

    @Override
    public Resp<List<BankAccountTypeVo>> getBankAccountType(Long userId) {
        List<BankAccount> list = bankAccountService.list();
        List<BankAccountTypeVo> bankAccountTypeVos = TransUtil.transList(list, BankAccountTypeVo.class);
        return Resp.successData(bankAccountTypeVos);
    }

    @Override
    public Resp<Void> addBankAccountType(BankUserReq data) {
        Long id = data.getId();
        if(id == null){
            BankUser bankUser = TransUtil.transEntity(data, BankUser.class);
            Integer num = bankUserService.count(new QueryWrapper<BankUser>().eq("bank_account_id", data.getBankAccountId()).eq("user_id",data.getUserId()));
            if(num > 0){
                return Resp.error("已有该账号类型");
            }
            bankUserService.save(bankUser);
        }else {
            BankUser bankUser = TransUtil.transEntity(data, BankUser.class);
            bankUserService.updateById(bankUser);
        }
        return Resp.success();
    }

    @Override
    public Resp<List<BankUserVo>> getUserBankAccountList(Long userId) {
        List<BankUser> bankUsers = bankUserService.list(new QueryWrapper<BankUser>().eq("user_id", userId));
        List<BankUserVo> bankUserVos = TransUtil.transList(bankUsers, BankUserVo.class);
        for (BankUserVo bankUserVo : bankUserVos) {
            BankAccount byId = bankAccountService.getById(bankUserVo.getBankAccountId());
            bankUserVo.setBankAccountName(byId==null?"":byId.getName());
        }
        return Resp.successData(bankUserVos);
    }

    @Override
    public Resp<Void> deleteUserBankAccount(BankUserVo data) {
        boolean b = bankUserService.removeById(data.getId());
        return Resp.success();
    }

    @Override
    public Resp<List<LiveGiftRankingVo>> getLiveGiftRanking(Long data) {
        int live_record_id = this.count(new QueryWrapper<UserWalletRecord>().eq("live_record_id", data));
        List<LiveGiftRankingVo> liveGiftRankingVos = new ArrayList<>();
        List<LiveGiftRankingVo> gRanking = baseMapper.getLiveGiftRanking(data);
        log.info("钱包记录送礼的========gRanking【{}】", JSON.toJSONString(gRanking));

        if(CollectionUtils.isNotEmpty(gRanking)){
            int size = gRanking.size();
            log.info("size【{}】", JSON.toJSONString(size));
            if(size < 3){
                List<Long> userIds = gRanking.stream().map(LiveGiftRankingVo::getUserId).collect(Collectors.toList());
                log.info("userIds【{}】", userIds);
                List<LiveGiftRankingVo> liveGiftRanking = watchRecordService.getLiveGiftRanking(data, 10,userIds);
                //List<LiveGiftRankingVo> liveGiftRanking = userWalletRecordMapper.getLiveGiftRanking(data);
                log.info("liveGiftRanking===【{}】", liveGiftRanking);
                for (LiveGiftRankingVo liveGiftRankingVo : liveGiftRanking) {
                    boolean contains = userIds.contains(liveGiftRankingVo);
                    if(!contains){
                        gRanking.add(liveGiftRankingVo);
                    }
                }

            }

            //List<LiveGiftRankingVo> subList = gRanking.subList(0, 3);
            //liveGiftRankingVos.addAll(subList);

            liveGiftRankingVos.addAll(gRanking);
        }else {
            List<LiveGiftRankingVo> liveGiftRanking = watchRecordService.getLiveGiftRanking(data, 3,new ArrayList<>());
            //List<LiveGiftRankingVo> liveGiftRanking = userWalletRecordMapper.getLiveGiftRanking(data);
            log.info("大于3人【{}】", liveGiftRanking);
            liveGiftRankingVos.addAll(liveGiftRanking);

        }

        return Resp.successData(liveGiftRankingVos);
    }
}
