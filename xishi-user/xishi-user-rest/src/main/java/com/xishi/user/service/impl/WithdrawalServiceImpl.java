package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.MakeOrderNum;
import com.common.base.util.ToolUtil;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.req.WithdrawalRecordReq;
import com.xishi.user.entity.req.WithdrawalSubmitReq;
import com.xishi.user.entity.vo.AgentApplyVo;
import com.xishi.user.entity.vo.WithdrawalVo;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.WithdrawalMapper;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 提现 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-08
 */
@Service
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements IWithdrawalService {

    @Autowired
    IUserService userService;
    @Autowired
    private IBankAccountService bankAccountService;
    @Autowired
    private IBankUserService bankUserService;
    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private IAgentService agentService;

    @Override
    public Resp<List<WithdrawalVo>> withdrawalRecord(WithdrawalRecordReq data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<Withdrawal> wrapper = new QueryWrapper();
        wrapper.eq("user_id",data.getUserId());
        wrapper.orderByDesc("id");
        List<Withdrawal> list = this.list(wrapper);
        List<WithdrawalVo> withdrawalVos = TransUtil.transList(list, WithdrawalVo.class);
        for (WithdrawalVo withdrawalVo : withdrawalVos) {
            BankUser byId = bankUserService.getById(withdrawalVo.getAccount());
            if(byId != null){
                BankAccount bankAccount = bankAccountService.getById(byId.getBankAccountId());
                withdrawalVo.setAccountName(bankAccount.getName());
            }
        }
        return Resp.successData(withdrawalVos);
    }

    @Override
    @Transactional
    public Resp<Void> withdrawalSubmit(WithdrawalSubmitReq data) {
        BigDecimal amount = data.getAmount();
        if(amount == null || BigDecimal.ZERO.compareTo(amount) > 0){
            return Resp.error("请输入正确的提现金额");
        }
        Long userId = data.getUserId();

        User byId = userService.getById(userId);
        Resp<Void> voidResp = userService.checkUser(byId);
        if(!voidResp.isSuccess()){
            return Resp.error(voidResp.getMsg());
        }

        UserWallet walletByUserId = userWalletService.findWalletByUserId(userId);
        BigDecimal giftAmount = walletByUserId.getGiftAmount();
        if(amount.compareTo(giftAmount)>0){
            return Resp.error("可提现金额不足");
        }
        //扣西施币
        BigDecimal subtract = giftAmount.subtract(amount);
        walletByUserId.setGiftAmount(subtract);
        userWalletService.updateById(walletByUserId);

        this.insertWithdrawal(data);
        return Resp.success("提交成功");
    }

    public boolean insertWithdrawal(WithdrawalSubmitReq data){
        Withdrawal insert = new Withdrawal();
        String randomNum = MakeOrderNum.makeOrderNum("EX");
        insert.setUserId(data.getUserId());
        insert.setMoney(data.getAmount());
        insert.setAccount(data.getAccountId()+"");
        insert.setOrderNum(randomNum);
        User byId = userService.getById(data.getUserId());
        if(byId.getApplyAgent() == 1){
            if(byId.getIsCreateAgent() == 1){
                insert.setType(1);
            }
            if(byId.getIsCreateAgent() == 0){
                insert.setType(2);
            }
            insert.setAgentId(byId.getNewestApplyAgentId());
        }

        this.save(insert);
        return true;
    }
}
