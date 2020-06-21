package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 收货地址 Mapper 接口
 */
@Repository
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 查询一个默认地址
     *
     * @param userId
     * @return
     */
    UserAddress selectDefaultAddress(@Param("userId") Long userId);

    /**
     * 取消设置默认地址
     *
     * @param userId
     */
    void cancelDefaultAddress(@Param("userId") Long userId);
}
