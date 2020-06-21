package com.xishi.user.dao.mapper;

import com.xishi.user.model.pojo.AgentWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-12-25
 */
public interface AgentWalletMapper extends BaseMapper<AgentWallet> {

    void addMoneyById(@Param("belongAgent") Long belongAgent,@Param("rateMoney") BigDecimal rateMoney);

    void addGIftMoneyById(@Param("belongAgent") Long belongAgent,@Param("rateMoney") BigDecimal rateMoney);

    void subMoneyById(@Param("belongAgent") Long belongAgent,@Param("subMoney") BigDecimal subMoney);
}
