package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.config.redisson.RedissonUtil;
import com.common.base.model.Resp;
import com.common.base.util.StrKit;
import com.common.base.util.ToolUtil;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.req.AgentApplyReq;
import com.xishi.user.entity.req.AgentInviteReq;
import com.xishi.user.entity.req.AgentQuery;
import com.xishi.user.entity.vo.*;
import com.xishi.user.enums.ApplyAgentEnum;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.AgentMapper;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 代理商 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-21
 */
@Slf4j
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements IAgentService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAgentInviteService agentInviteService;

    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private IAgentDealRecordService agentDealRecordService;
    @Autowired
    private IAttentionService attentionService;
    @Autowired
    private IAgentService agentService;

    @Override
    public Resp<AgentApplyVo> applyAgent(AgentApplyReq data) {
        Agent agent = TransUtil.transEntity(data, Agent.class);
        User byId = userService.getById(agent.getUserId());
        Agent dbAgent = null;
        String randomNum="";
        if(StrKit.isNotEmpty(data.getAgentNum())){ //重新申请
            dbAgent = this.getOne(new QueryWrapper<Agent>().eq("agent_num", data.getAgentNum()));
        }
        if(dbAgent !=null){
            agent.setId(dbAgent.getId());
            agent.setAgentNum(dbAgent.getAgentNum());
            agent.setAuditState(0);
            randomNum = dbAgent.getAgentNum();
            this.updateById(agent);
        }else {
            randomNum = this.genAgentNum();
            agent.setAgentNum(randomNum);
            agent.setAgentUserName(byId.getName());
            agent.setAgentPhone(byId.getPhone());
            this.save(agent);
        }
        //创建处理信息
        AgentDealRecord agentDealRecord = new AgentDealRecord();
        agentDealRecord.setAgentId(agent.getId());
        agentDealRecord.setRemark("（"+byId.getXishiNum()+"）发起申请");
        agentDealRecordService.save(agentDealRecord);
        //同步用户家族创建状态
        byId.setApplyAgent(ApplyAgentEnum.ING_APPLY.getCode());//申请中
        byId.setUpdateTime(new Date());
        byId.setNewestApplyAgentId(agent.getId());
        userService.updateById(byId);
        //封装返回消息
        AgentApplyVo agentApplyVo = this.genAgentApplyVo(agent,byId);
        agentApplyVo.setAgentNum(randomNum);
        return Resp.successData(agentApplyVo);
    }

    @Override
    public Resp<AgentApplyVo> getMyAgentInfo(Long userId) {
        User byId = userService.getById(userId);
        if(byId == null){
            return Resp.error("用户不存在");
        }
        Long dbBelong = byId.getBelongAgent();
        Agent belongAgent = this.getById(byId.getNewestApplyAgentId());
        if(dbBelong != 0){ //有所属代理商
            belongAgent = this.getById(dbBelong);
        }
        //打理人
        User manager = userService.getById(belongAgent.getUserId());
        AgentApplyVo agentApplyVo = this.genAgentApplyVo(belongAgent,manager);

        int count = attentionService.count(new QueryWrapper<Attention>().eq("user_id", userId).eq("link_id",belongAgent.getUserId()));
        agentApplyVo.setAttention(count>0?1:0);
        return Resp.successData(agentApplyVo);
    }

    @Override
    public Resp<List<AgentShowerVo>> getAgentShowerInfo(AgentQuery data) {
        Long userId = data.getUserId();
        User byId = userService.getById(userId);
        //Agent belongAgent = this.getById(byId.getBelongAgent());

        log.info("User【{}】", byId);

        Agent belongAgent = baseMapper.selectById(byId.getBelongAgent());

        log.info("belongAgent【{}】", belongAgent);
        log.info("userId【{}】", belongAgent.getUserId());
        log.info("belong_agent【{}】", byId.getBelongAgent());

        // 查询相同家族的用户
        PageHelper.startPage(data.getPage(),data.getRows());
        List<User> showers = userService.list(new QueryWrapper<User>().notIn("id",belongAgent.getUserId()).eq("belong_agent", byId.getBelongAgent()).eq("state", 0));
        log.info("showers【{}】", showers);

        List<AgentShowerVo> agentShowerVos = new ArrayList<>();
        for (User shower : showers) {
            AgentShowerVo agentShowerVo = new AgentShowerVo();
            agentShowerVo.setAgentNum(belongAgent.getAgentNum());
            int count = attentionService.count(new QueryWrapper<Attention>().eq("user_id", userId).eq("link_id",shower.getId()));
            agentShowerVo.setAttention(count>0?1:0);
            agentShowerVo.setShowerAvatar(shower.getAvatar());
            agentShowerVo.setShowerUserId(shower.getId());
            agentShowerVo.setShowerIntro(shower.getIntro());
            agentShowerVo.setShowerName(shower.getName());
            agentShowerVo.setShowerXishiNum(shower.getXishiNum());
            agentShowerVos.add(agentShowerVo);
        }
        return Resp.successData(agentShowerVos);
    }

    @Override
    public Resp<List<AgentSearchVo>> searchAgent(AgentQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        String keyWord = data.getKeyWord();
        QueryWrapper<Agent> wrapper = new QueryWrapper();
        if(StrKit.isNotEmpty(keyWord)) {
            wrapper.like("agent_name",keyWord).or().like("agent_num",keyWord);
        }
        List<Agent> list = this.list(wrapper);
        List<AgentSearchVo> agentSearchVos = TransUtil.transList(list, AgentSearchVo.class);
        return Resp.successData(agentSearchVos);
    }

    @Override
    public Resp<List<AgentSearchUserVo>> searchUser(AgentQuery data) {
        Agent agent = agentService.getOne(new QueryWrapper<Agent>().eq("user_id", data.getUserId()).eq("audit_state", 1).eq("state", 0));
        if(agent == null){
            return Resp.error("你不是家族打理人");
        }
        List<AgentInvite> agentInvites = agentInviteService.list(new QueryWrapper<AgentInvite>().eq("agent_num", agent.getAgentNum()).ne("join_state",2));
        List<Long> userIds = agentInvites.stream().map(AgentInvite::getUserId).collect(Collectors.toList());
        userIds.add(data.getUserId());
        final String keyWord = data.getKeyWord();
        QueryWrapper<User> wrapper = new QueryWrapper();
        if(!userIds.isEmpty()){
            wrapper.notIn("id",userIds);
        }
        if(StrKit.isNotEmpty(keyWord)) {
            wrapper.and(i->i.like("name",keyWord).or().like("xishi_num",keyWord));
        }
        wrapper.eq("shower",1);
        PageHelper.startPage(data.getPage(),data.getRows());
        List<User> list = userService.list(wrapper);
        List<AgentSearchUserVo> agentSearchUserVos = TransUtil.transList(list, AgentSearchUserVo.class);
        for (AgentSearchUserVo agentSearchUserVo : agentSearchUserVos) {
            AgentInvite one = agentInviteService.getOne(new QueryWrapper<AgentInvite>().eq("user_id", agentSearchUserVo.getId()).eq("agent_num", agent.getAgentNum()).in("type", 1,2));
            if(one != null){
                agentSearchUserVo.setState(1);
                agentSearchUserVo.setJoinState(one.getJoinState());
            }
        }
        return Resp.successData(agentSearchUserVos);
    }

    @Override
    public Resp<List<AgentInviteAgentVo>> inviteRecord(AgentQuery data) {
        //查询用户所有被邀请记录
        PageHelper.startPage(data.getPage(),data.getRows());
        List<AgentInvite> list = agentInviteService.list(new QueryWrapper<AgentInvite>().eq("user_id", data.getUserId()).eq("type", 1));
        List<AgentInviteAgentVo> agentInviteAgentVos = new ArrayList<>();
        for (AgentInvite agentInvite : list) {
            String agentNum = agentInvite.getAgentNum();
            Agent dbAgent = this.getOne(new QueryWrapper<Agent>().eq("agent_num", agentNum));
            //组装家族信息
            AgentInviteAgentVo agentInviteAgentVo = TransUtil.transEntity(dbAgent, AgentInviteAgentVo.class);
            agentInviteAgentVo.setCreateTime(agentInvite.getCreateTime());
            agentInviteAgentVo.setJoinState(agentInvite.getJoinState());
            agentInviteAgentVos.add(agentInviteAgentVo);
        }
        return Resp.successData(agentInviteAgentVos);
    }

    @Override
    public Resp<Void> agentInviteUser(AgentInviteReq data) {
        Long linkUserId = data.getLinkUserId();
        if(linkUserId ==null){
            return Resp.error("参数错误");
        }
        User byId = userService.getById(data.getUserId());
        Long belongAgentId = byId.getBelongAgent();
        Agent belongAgent = this.getById(belongAgentId);
        this.genAgentInvite(belongAgent,data.getLinkUserId(),1);//打理人邀请
        return Resp.success();
    }

    @Override
    public Resp<Void> userApply(AgentInviteReq data) {
        if(StrKit.isEmpty(data.getAgentNum())){
            return Resp.error("参数错误");
        }
        Agent agent = this.getOne(new QueryWrapper<Agent>().eq("agent_num", data.getAgentNum()));
        if(agent == null){
            return Resp.error("参数错误");
        }
        this.genAgentInvite(agent,data.getUserId(),0);//用户申请
        return Resp.success();
    }

    @Override
    public Resp<List<AgentInviteUserVo>> agentInviteRecord(AgentQuery data) {
        //查询家族所有邀请信息
        User byId = userService.getById(data.getUserId());
        Agent belongAgent = this.getById(byId.getBelongAgent());

        PageHelper.startPage(data.getPage(),data.getRows());
        List<AgentInvite> list = agentInviteService.list(new QueryWrapper<AgentInvite>().eq("agent_num", belongAgent.getAgentNum()).eq("type", 1).orderByDesc("id"));
        //封装邀请信息
        List<AgentInviteUserVo> agentInviteUserVos = this.genAgentInviteUserVoList(list);
        return Resp.successData(agentInviteUserVos);
    }

    public List<AgentInviteUserVo> genAgentInviteUserVoList(List<AgentInvite> list){
        List<AgentInviteUserVo> agentInviteUserVos = new ArrayList<>();


        for (AgentInvite agentInvite : list) {
            Long userId = agentInvite.getUserId();
            log.info("userId:【{}】", userId);
            User dbUser = userService.getById(userId);
            log.info("dbUser:【{}】", dbUser);
            AgentInviteUserVo agentInviteUserVo = TransUtil.transEntity(dbUser, AgentInviteUserVo.class);
            agentInviteUserVo.setUserId(userId);
            agentInviteUserVo.setJoinState(agentInvite.getJoinState());
            agentInviteUserVo.setCreateTime(agentInvite.getCreateTime());
            agentInviteUserVos.add(agentInviteUserVo);
        }
        return agentInviteUserVos;
    }

    @Override
    public Resp<Void> shutdownApply(AgentInviteReq data) {
        Long linkUserId = data.getLinkUserId();
        String agentNum = data.getAgentNum();
        Agent agent = this.getOne(new QueryWrapper<Agent>().eq("agent_num", agentNum));
        if(linkUserId ==null || agent == null){
            return Resp.error("参数错误");
        }
        User byId = userService.getById(linkUserId);
        byId.setBelongAgent(0L);
        byId.setApplyAgent(0);
        byId.setNewestApplyAgentId(0L);
        byId.setUpdateTime(new Date());
        userService.updateById(byId);
        //更新家族数量
        agent.setShowerNum(agent.getShowerNum()-1);
        this.updateById(agent);
        //删除邀请记录
        agentInviteService.remove(new QueryWrapper<AgentInvite>().eq("agent_num", agentNum).eq("user_id", linkUserId));
        return Resp.success();
    }

    @Override
    public Resp<Void> agreeInvite(AgentInviteReq data) {
        Integer joinState = data.getJoinState();
        String agentNum = data.getAgentNum();
        Long userId = data.getUserId();
        Agent agent = this.getOne(new QueryWrapper<Agent>().eq("agent_num", agentNum));
        if(joinState == 1){
                //同意加入家族
            String agreeKey = RedisConstants.AGREE_AGENT_INVITE + data.getUserId();
            boolean locked = redissonUtil.lock(agreeKey, 60);
            if (!locked) {
                return Resp.error("加入家族失败，请稍后重试");
            }
            try {
                //判断是否存在所属家族
                User byId = userService.getById(userId);
                Long belongAgent = byId.getBelongAgent();
                if (belongAgent != 0) {
                    return Resp.error("您已有所属家族");
                }
                if(agent.getState() == 1){
                    return Resp.error("该家族已被禁用");
                }
                //加入家族
                byId.setBelongAgent(agent.getId());
                byId.setUpdateTime(new Date());
                userService.updateById(byId);
                //更新家族数量
                agent.setShowerNum(agent.getShowerNum()+1);
                this.updateById(agent);

            }finally {
                redissonUtil.unlock(agreeKey);
            }
        }
        //更新邀请记录
        AgentInvite one = agentInviteService.getOne(new QueryWrapper<AgentInvite>().eq("agent_num", agentNum).eq("user_id", userId).eq("type", 1));
        if(one != null){
            one.setJoinState(joinState);
            agentInviteService.updateById(one);
        }
        return Resp.success();
    }

    public void genAgentInvite(Agent agent,Long linkUserId,Integer type){
        AgentInvite one = agentInviteService.getOne(new QueryWrapper<AgentInvite>().eq("agent_num", agent.getAgentNum()).eq("user_id", linkUserId).eq("type", type));
        if(one == null){
            AgentInvite agentInvite = new AgentInvite();
            agentInvite.setAgentNum(agent.getAgentNum());
            agentInvite.setUserId(linkUserId);
            agentInvite.setAgentId(agent.getId());
            agentInvite.setType(type);
            agentInviteService.save(agentInvite);
        }else {
            one.setJoinState(0);
            one.setState(0);
            agentInviteService.updateById(one);
        }
    }

    public AgentApplyVo genAgentApplyVo(Agent agent,User byId){
        AgentApplyVo agentApplyVo = TransUtil.transEntity(agent, AgentApplyVo.class);
        agentApplyVo.setAgentUserIntro(byId.getIntro());
        agentApplyVo.setAgentUserAvatar(byId.getAvatar());
        agentApplyVo.setAgentUserXishiNum(byId.getXishiNum());
        int belong_agent = userService.count(new QueryWrapper<User>().eq("belong_agent", agent.getId()));
        agentApplyVo.setShowerNum(belong_agent-1);
        return agentApplyVo;
    }

    //生成家族编号
    public String genAgentNum() {
        String agentNum = ToolUtil.getRandomNum(12);
        Agent agent = this.getOne(new QueryWrapper<Agent>().eq("agent_num",agentNum));
        while (agent != null) {
            agentNum = ToolUtil.getRandomNum(12);
            agent = this.getOne(new QueryWrapper<Agent>().eq("agent_num",agentNum));
        }
        return agentNum;
    }

}
