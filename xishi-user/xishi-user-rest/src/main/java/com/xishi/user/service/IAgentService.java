package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.AgentApplyReq;
import com.xishi.user.entity.req.AgentInviteReq;
import com.xishi.user.entity.req.AgentQuery;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.Agent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 代理商 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-21
 */
public interface IAgentService extends IService<Agent> {

    Resp<AgentApplyVo> applyAgent(AgentApplyReq data);

    Resp<AgentApplyVo> getMyAgentInfo(Long userId);

    Resp<List<AgentShowerVo>> getAgentShowerInfo(AgentQuery data);

    Resp<List<AgentSearchVo>> searchAgent(AgentQuery data);

    Resp<List<AgentSearchUserVo>> searchUser(AgentQuery data);

    Resp<List<AgentInviteAgentVo>> inviteRecord(AgentQuery data);

    Resp<List<AgentInviteUserVo>> agentInviteRecord(AgentQuery data);

    Resp<Void> agentInviteUser(AgentInviteReq data);

    Resp<Void> userApply(AgentInviteReq data);

    Resp<Void> shutdownApply(AgentInviteReq data);

    Resp<Void> agreeInvite(AgentInviteReq data);
}
