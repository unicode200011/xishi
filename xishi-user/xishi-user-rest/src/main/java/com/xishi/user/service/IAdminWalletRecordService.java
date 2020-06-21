package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.entity.mqMessage.AdminMoneyRabbitMessage;
import com.xishi.user.model.pojo.AdminWalletRecord;

/**
 * <p>
 * 后台钱包记录 服务类
 * </p>
 *
 * @author MS
 * @since 2020-5-2
 */
public interface IAdminWalletRecordService extends IService<AdminWalletRecord> {
    void adminMoneyService(AdminMoneyRabbitMessage adminMoneyRabbitMessage);

}
