package com.xishi.user.service.impl;

import com.xishi.user.model.pojo.UserAuthInfo;
import com.xishi.user.dao.mapper.UserAuthInfoMapper;
import com.xishi.user.service.IUserAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户实名认证信息 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements IUserAuthInfoService {

}
