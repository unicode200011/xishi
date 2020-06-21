package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.base.model.BaseQuery;
import com.common.base.model.Resp;
import com.common.base.util.StrKit;
import com.xishi.user.dao.mapper.UserAddressMapper;
import com.xishi.user.model.pojo.User;
import com.xishi.user.model.pojo.UserAddress;
import com.xishi.user.service.IUserAddressService;
import com.xishi.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private IUserService userService;

    @Override
    public List<UserAddress> addressList(BaseQuery query) {
        Long userId = query.getUserId();
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("type","id");

        long pageNo = new Integer(query.getPage()==null?1:query.getPage()).longValue();
        long rows = new Integer(query.getRows()==null?5:query.getRows()).longValue();
        IPage<UserAddress> page = new Page<UserAddress>(pageNo,rows);
        IPage<UserAddress> pageResult = userAddressMapper.selectPage(page,queryWrapper);
        List<UserAddress> list = pageResult.getRecords();
        return list;
    }

    @Override
    public UserAddress defaultAddress(BaseQuery query) {
        UserAddress userAddress = userAddressMapper.selectDefaultAddress(query.getUserId());
        return userAddress;
    }

    @Override
    public Resp<Void> changeAddress(UserAddress address) {
        log.info("UserAddressServiceImpl changeAddress start,address info={}",address);
        Long addressId = address.getId();
        Long userId = address.getUserId();
        String name = address.getName();
        String phone = address.getPhone();
        String detailAddress = address.getDetailAddress();
        String addressStr = address.getAddress();
        Integer type = address.getType() == null ? 0 : address.getType();
        Integer opType = address.getOpType() == null ? 0 : address.getOpType();
        if (opType == 1) {
            UserAddress dbAddress = userAddressMapper.selectById(addressId);
            if (dbAddress == null) {
                return Resp.error("地址不存在");
            }
            if (!userId.equals(dbAddress.getUserId())) {
                return Resp.error("无权限操作");
            }
            userAddressMapper.deleteById(addressId);

            //删除完毕如没有默认地址,设置默认地址
            UserAddress userAddress = userAddressMapper.selectDefaultAddress(userId);
            if (userAddress == null) {
                QueryWrapper<UserAddress> ew = new QueryWrapper();
                ew.eq("user_id", userId);
                ew.orderByDesc("type", "id");
                ew.last(" LIMIT 1 ");
                List<UserAddress> userAddresses = userAddressMapper.selectList(ew);
                if (!userAddresses.isEmpty()) {
                    UserAddress updateDefaultAddress = userAddresses.get(0);
                    updateDefaultAddress.setType(1);
                    userAddressMapper.updateById(updateDefaultAddress);
                }
            }
            return Resp.success("删除成功");
        }

        if (addressId != null && addressId > 0L) {
            UserAddress dbAddress = userAddressMapper.selectById(addressId);
            if (dbAddress == null) {
                return Resp.error("地址不存在");
            }
            if (!userId.equals(dbAddress.getUserId())) {
                return Resp.error("无权限操作");
            }
            //设置默认地址
            if (type == 1 && dbAddress.getType() != 1) {
                //取消所有默认地址
                userAddressMapper.cancelDefaultAddress(userId);
                dbAddress.setType(type);
            }
            if (StrKit.isNotEmpty(name)) {
                dbAddress.setName(name);
            }
            if (StrKit.isNotEmpty(phone)) {
                dbAddress.setPhone(phone);
            }
            if (StrKit.isNotEmpty(addressStr)) {
                dbAddress.setAddress(addressStr);
            }
            if (StrKit.isNotEmpty(detailAddress)) {
                dbAddress.setDetailAddress(detailAddress);
            }
            dbAddress.setUpdateTime(new Date());
            userAddressMapper.updateById(dbAddress);
            return Resp.success("修改成功");
        } else {

            if (StrKit.isEmpty(name)) {
                return Resp.error("姓名不能为空");
            }
            if (StrKit.isEmpty(phone)) {
                return Resp.error("手机号不能为空");
            }
            if (StrKit.isEmpty(addressStr)) {
                return Resp.error("地址不能为空");
            }
            if (StrKit.isEmpty(detailAddress)) {
                return Resp.error("详细地址不能为空");
            }
            User user = userService.getById(userId);
            if (user == null) {
                return Resp.error("用户不存在");
            }

            address.setCreateTime(new Date());
            address.setUpdateTime(new Date());
            address.setType(type);
            if(type==1) {
                userAddressMapper.cancelDefaultAddress(userId);
            } else {
                UserAddress userAddress = userAddressMapper.selectDefaultAddress(userId);
                if (userAddress == null) {
                    address.setType(1);
                }
            }
            userAddressMapper.insert(address);
            return Resp.success("添加成功");
        }
    }

}
