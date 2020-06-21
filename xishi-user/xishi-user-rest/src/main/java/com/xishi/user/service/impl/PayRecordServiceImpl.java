package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.dao.mapper.PayRecordMapper;
import com.xishi.user.model.pojo.PayRecord;
import com.xishi.user.service.IPayRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements IPayRecordService {

    public boolean updatePayRecord(Long userId, String orderNo, String tradeNo,Integer state, String reason) {
        QueryWrapper<PayRecord> query = new QueryWrapper<PayRecord>();
        query.eq("user_id",userId);
        query.eq("order_no",orderNo);
        PayRecord payRecord = baseMapper.selectOne(query);
        if (payRecord != null) {
            payRecord.setUpdateTime(new Date());
            payRecord.setTradeNo(tradeNo);
            payRecord.setState(state);
            payRecord.setReason(reason);
            baseMapper.updateById(payRecord);
            return true;
        }
        return false;
    }

}
