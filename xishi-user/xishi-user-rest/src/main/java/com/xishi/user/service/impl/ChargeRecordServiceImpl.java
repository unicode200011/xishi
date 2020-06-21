package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.req.ChargeRecordReq;
import com.xishi.user.entity.vo.ChargeRecordVo;
import com.xishi.user.model.pojo.ChargeRecord;
import com.xishi.user.dao.mapper.ChargeRecordMapper;
import com.xishi.user.service.IChargeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper, ChargeRecord> implements IChargeRecordService {

    @Override
    public Resp<List<ChargeRecordVo>> getUserChargeRecord(ChargeRecordReq data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<ChargeRecord> queryWrapper = new QueryWrapper<ChargeRecord>();
        queryWrapper.eq("user_id", data.getUserId());
        queryWrapper.orderByDesc("id");
        List<ChargeRecord> list = this.list(queryWrapper);
        List<ChargeRecordVo> chargeRecordVos = TransUtil.transList(list, ChargeRecordVo.class);
        for (ChargeRecordVo chargeRecordVo : chargeRecordVos) {
            chargeRecordVo.setSourceName(chargeRecordVo.getSource() == 0 ?"微信":"支付宝");
        }
        Resp<List<ChargeRecordVo>> resp = new Resp<List<ChargeRecordVo>>(chargeRecordVos);
        return resp;
    }
}
