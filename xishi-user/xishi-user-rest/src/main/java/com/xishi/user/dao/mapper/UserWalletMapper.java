package com.xishi.user.dao.mapper;

import com.xishi.user.model.pojo.UserWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Component
public interface UserWalletMapper extends BaseMapper<UserWallet> {

    BigDecimal findByUserId(@Param("userId") Long userId);

    void upByUserId(@Param("userId") Long userId , @Param("inMoney") BigDecimal inMoney);

    void subByUserId(@Param("userId") Long userId , @Param("inMoney") BigDecimal inMoney);

    UserWallet findWalletByUserId(@Param("userId") Long userId);

    String findCommon();

    void addGiveAmount(@Param("userId") Long userId , @Param("inMoney") BigDecimal inMoney);

    void addGiftAmount(@Param("userId") Long userId , @Param("inMoney") BigDecimal inMoney);
}
