package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.BankCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 银行卡 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2018-10-13
 */
@Repository
public interface BankCardMapper extends BaseMapper<BankCard> {

    /**
     * 查询用户银行卡
     *
     * @param userId
     * @return
     */
    List<BankCard> queryByUserId(@Param("userId") Long userId);
}
