package com.xishi.liveShow.feign;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.liveShow.entity.req.LiveGiftListQuery;
import com.xishi.liveShow.entity.vo.LiveGiftListVo;
import com.xishi.user.entity.req.UserAttentionQuery;
import com.xishi.user.entity.req.UserSearchQuery;
import com.xishi.user.entity.vo.UserInfoVo;
import com.xishi.user.entity.vo.UserWalletVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "xishi-user")
public interface UserService {

    @PostMapping("/user/queryUserInfo")
    Resp<UserInfoVo> queryUserInfo(Req<Long> req);

    @PostMapping("/user/checkAttention")
    Resp<Integer> checkAttention(Req<UserAttentionQuery> req);

    @PostMapping("/user/searchUser")
    Resp<List<UserInfoVo>> searchUser(Req<UserSearchQuery> req);
    /**
     * 查验用户钱包
     * @return
     */
    @PostMapping("/user/checkUserWallet")
    Resp<UserWalletVo> checkUserWallet(Req<Long> req);
    /**
     * 获取直播间贡献榜
     * @return
     */
    @PostMapping("/user/getLiveGiftList")
    Resp<List<LiveGiftListVo>> getLiveGiftList(Req<LiveGiftListQuery> req);
}
