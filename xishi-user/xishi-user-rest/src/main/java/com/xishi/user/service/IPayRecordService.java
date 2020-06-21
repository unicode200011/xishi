package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.model.pojo.PayRecord;

public interface IPayRecordService extends IService<PayRecord> {

    public boolean updatePayRecord(Long userId, String orderNum, String tradeNo, Integer state,String reason);
}
