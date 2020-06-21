package com.xishi.user.service.impl;

import com.xishi.user.model.pojo.Pay;
import com.xishi.user.dao.mapper.PayMapper;
import com.xishi.user.service.IPayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付通道 服务实现类
 * </p>
 *
 * @author LX
 * @since 2020-02-14
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements IPayService {

}
