package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.base.model.BaseQuery;
import com.common.base.model.Resp;
import com.xishi.user.model.pojo.Provinces;
import com.xishi.user.model.pojo.UserAddress;
import com.xishi.user.entity.req.CommonQuery;

import java.util.List;

public interface IUserAddressService extends IService<UserAddress>  {

    /**
     * 收货地址列表
     */
    List<UserAddress> addressList(BaseQuery query);

    /**
     * 获取默认地址
     */
    UserAddress defaultAddress(BaseQuery query);

    /**
     * 新增收货地址
     */
    Resp<Void> changeAddress(UserAddress address);

}
