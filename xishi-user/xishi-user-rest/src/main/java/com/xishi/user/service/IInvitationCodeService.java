package com.xishi.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xishi.user.model.pojo.InvitationCode;
import com.xishi.user.model.pojo.InvitationUser;

public interface IInvitationCodeService extends IService<InvitationCode> {

    /**
     * 邀请用户
     *
     * @param invitationCode
     * @param userId
     * @return
     */
    InvitationUser inviteUser(InvitationCode invitationCode, Long userId);

    /**
     * 创建邀请码
     *
     * @param userId
     * @param account
     */
    boolean createInvitationCode(Long userId, String account);


    public InvitationCode queryByCode(String code);

    public InvitationCode queryByUser(Long userId);

    public boolean genInviteQrCode(Long userId);

    public String getInviteRegUrl();
}
