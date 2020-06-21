package com.xishi.user.dao.mapper;

import com.xishi.user.model.pojo.ChargeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 充值列表 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface ChargeListMapper extends BaseMapper<ChargeList> {

    String findCommon();

}
