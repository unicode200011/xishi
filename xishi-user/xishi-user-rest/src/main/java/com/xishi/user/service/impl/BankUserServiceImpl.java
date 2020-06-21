package com.xishi.user.service.impl;

import com.xishi.user.model.pojo.BankUser;
import com.xishi.user.dao.mapper.BankUserMapper;
import com.xishi.user.service.IBankUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户银行账户信息 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-12-14
 */
@Service
public class BankUserServiceImpl extends ServiceImpl<BankUserMapper, BankUser> implements IBankUserService {

}
