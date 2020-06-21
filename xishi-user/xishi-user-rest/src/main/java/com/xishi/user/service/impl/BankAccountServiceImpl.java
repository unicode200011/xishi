package com.xishi.user.service.impl;

import com.xishi.user.model.pojo.BankAccount;
import com.xishi.user.dao.mapper.BankAccountMapper;
import com.xishi.user.service.IBankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户分类 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-12-14
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

}
