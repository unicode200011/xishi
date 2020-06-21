package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.PayRecord;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 支付记录(支付宝,微信) Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-02-16
 */
@Repository
public interface PayRecordMapper extends BaseMapper<PayRecord> {

}
