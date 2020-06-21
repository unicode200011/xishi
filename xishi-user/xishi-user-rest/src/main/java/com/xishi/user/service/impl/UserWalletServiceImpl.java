package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.config.redisson.RedissonUtil;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.xishi.socket.entity.Enum.MqUserServiceTypeConstens;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.*;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.UserWalletMapper;
import com.xishi.user.mq.MqUserSender;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Service
@Slf4j
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements IUserWalletService {

    @Autowired
    IUserWalletRecordService userWalletRecordService;
    @Autowired
    MqUserSender mqUserSender;
    @Autowired
    RedissonUtil redissonUtil;
    @Autowired
    RedisService redisService;
    @Autowired
    IUserService userService;
    @Autowired
    IAgentWalletService agentWalletService;
    @Autowired
    IAgentWalletRecordService agentWalletRecordService;
    @Autowired
    IUserWalletService userWalletService;
    @Autowired
    IAgentService agentService;

    @Autowired
    IAdminWalletRecordService adminWalletRecordService;

    @Override
    public BigDecimal findByUserId(Long userId) {
        return baseMapper.findByUserId(userId);
    }

    @Override
    public UserWallet findWalletByUserId(Long userId) {
        return baseMapper.findWalletByUserId(userId);
    }

    @Override
    public boolean addByUserId(Long userId, BigDecimal inMoney) {
        if(BigDecimal.ZERO.compareTo(inMoney) > 0){
            return false;
        }
        baseMapper.upByUserId(userId,inMoney);
        return true;
    }

    @Override
    public boolean subByUserId(Long userId, BigDecimal inMoney) {
        if(BigDecimal.ZERO.compareTo(inMoney) > 0){
            return false;
        }
        baseMapper.subByUserId(userId,inMoney);
        return true;
    }

    @Override
    public String findCommon() {
        return baseMapper.findCommon();
    }

    public static void main(String[] args) {
        BigDecimal[] bigDecimals = new BigDecimal(1888).divideAndRemainder(new BigDecimal(0.33));

        int v = new BigDecimal(623.04).intValue();
        float a = new BigDecimal(623.04).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        System.out.println(v);
        System.out.println(a);
        System.out.println(bigDecimals[0]);
        System.out.println(bigDecimals[1]);
    }

    @Override
    @Transactional
    public void sendGift(LiveGiftRabbitMessage liveGiftRabbitMessage) {
        //礼物总金额
        BigDecimal giftMoney = liveGiftRabbitMessage.getGiftMoney();
        //赠送者
        Long userId = liveGiftRabbitMessage.getUserId();
        User user = userService.getById(userId);
        //收益者
        Long linkUid = liveGiftRabbitMessage.getLinkUid();
        User shower = userService.getById(linkUid);

        String agreeKey = RedisConstants.USER_SEND_GIFT +linkUid;
        boolean locked = redissonUtil.lock(agreeKey, 60);
        if (!locked) {
            return;
        }
        try {
            //赠送者钱包余额
            BigDecimal dbMoney = this.findByUserId(userId);

            if(dbMoney.compareTo(giftMoney) >= 0){//钱包余额充足

                /**
                 * 用户
                 */
                //减少赠送者钱包余额
                this.subByUserId(userId,giftMoney);
                BigDecimal subtract = dbMoney.subtract(giftMoney);
                //修改消费总值
                baseMapper.addGiveAmount(userId,giftMoney);
                //新增钱包记录
                String userRemark = "打赏"+shower.getName();
                this.genWalletRecord(liveGiftRabbitMessage,1,1,subtract,userRemark,userId,linkUid);
                /**
                 * 主播
                 */
                Long belongAgent = shower.getBelongAgent();

                //加入家族主播
                if(belongAgent != 0){

                    //TODO 平台提成 并添加记录
                    Agent agent = agentService.getById(belongAgent);
                    //平台提成金额
                    BigDecimal adminMoney = giftMoney.multiply(agent.getAdminRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                    //平台提成后剩余金额
                    giftMoney = giftMoney.subtract(adminMoney);
                    //发送平台提成mq消息
                    adminSendMq(belongAgent, adminMoney,user.getName(),shower.getName(), liveGiftRabbitMessage);


                    //TODO 代理商提成 并增加记录
                    log.info("giftMoney:【{}】", giftMoney);
                    //代理提成金额
                    BigDecimal rateMoney = giftMoney.multiply(shower.getAgentRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                    //log.info("rateMoney:【{}】", rateMoney);



                    //发送代理商提成MQ消息
                    AgentMoneyRabbitMessage agentMoneyRabbitMessage = new AgentMoneyRabbitMessage();
                    agentMoneyRabbitMessage.setAgentId(belongAgent);
                    //agentMoneyRabbitMessage.setRateMoney(rateMoney);
                    agentMoneyRabbitMessage.setRateMoney(giftMoney);
                    agentMoneyRabbitMessage.setType(2);
                    agentMoneyRabbitMessage.setMessageType(0);
                    agentMoneyRabbitMessage.setServiceType(0);
                    agentMoneyRabbitMessage.setRemark(user.getName()+"打赏"+shower.getName()+liveGiftRabbitMessage.getNum()+"个"+liveGiftRabbitMessage.getGiftName());
                    agentMoneyRabbitMessage.setData(JSONObject.toJSONString(liveGiftRabbitMessage));
                    mqUserSender.sendAddAgentMoneyMessage(agentMoneyRabbitMessage);

                    //提成后剩余金额
                    giftMoney = giftMoney.subtract(rateMoney);
                    log.info("giftMoney:【{}】", giftMoney);
                }

                //平台主播
                if(belongAgent == 0){
                    //TODO 平台提成 并添加记录
                    BigDecimal adminMoney = giftMoney.multiply(user.getAgentRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                    giftMoney = giftMoney.subtract(adminMoney);
                    //发送平台提成mq消息
                    adminSendMq(belongAgent, adminMoney,user.getName(),shower.getName(), liveGiftRabbitMessage);
                }

                //增加被赠送者钱包余额
                BigDecimal dbLinkMoney = this.findByUserId(linkUid);
//                this.addByUserId(linkUid,giftMoney);
                BigDecimal add = dbLinkMoney.add(giftMoney);
                //修改合计收到打赏（西施币）
                baseMapper.addGiftAmount(linkUid,giftMoney);
                //新增钱包记录
                String showerRemark = user.getName()+"打赏";
                liveGiftRabbitMessage.setGiftMoney(giftMoney);
                this.genWalletRecord(liveGiftRabbitMessage,2,0,add,showerRemark,linkUid,userId);

                //更新本场收益缓存 直播结束时统一写入直播记录
                String liveTotalAmountKey = RedisConstants.LIVE_TOTAL_AMOUNT+liveGiftRabbitMessage.getLiveRecordId();
                Object cacheAmountObj = redisService.get(liveTotalAmountKey);
                BigDecimal cacheAmount = BigDecimal.ZERO;
                if(cacheAmountObj != null){
                    cacheAmount = (BigDecimal) cacheAmountObj;
                    cacheAmount = cacheAmount.add(giftMoney);
                }else {
                    cacheAmount = giftMoney;
                }
                redisService.set(liveTotalAmountKey,cacheAmount);

                //发送用户等级MQ消息
                mqUserSender.sendAddUserGradeMessage(new UserRabbitMessage(MqUserServiceTypeConstens.USER_GRADE,giftMoney,userId));

                //发送直播等级MQ消息
                mqUserSender.sendAddLiveGradeMessage(new UserRabbitMessage(MqUserServiceTypeConstens.LIVE_GRADE,giftMoney,linkUid));
            }
        } finally {
            redissonUtil.unlock(agreeKey);
        }
    }

    private void adminSendMq(Long userId, BigDecimal adminMoney, String userName, String showerName, LiveGiftRabbitMessage liveGiftRabbitMessage){
        //发送代理商提成MQ消息
        AdminMoneyRabbitMessage admintMoneyRabbitMessage = new AdminMoneyRabbitMessage();
        admintMoneyRabbitMessage.setUserId(userId);
        admintMoneyRabbitMessage.setRateMoney(adminMoney);
        //agentMoneyRabbitMessage.setRateMoney(giftMoney);
        admintMoneyRabbitMessage.setType(2);
        admintMoneyRabbitMessage.setMessageType(0);
        admintMoneyRabbitMessage.setServiceType(0);
        admintMoneyRabbitMessage.setRemark(userName+"打赏"+showerName+liveGiftRabbitMessage.getNum()+"个"+liveGiftRabbitMessage.getGiftName());
        admintMoneyRabbitMessage.setData(JSONObject.toJSONString(liveGiftRabbitMessage));
        mqUserSender.sendAddAdminMoneyMessage(admintMoneyRabbitMessage);
    }

    @Override
    @Transactional
    public void sendLivePay(LivePayRabbitMessage livePayRabbitMessage) {

        //单价
        BigDecimal price = livePayRabbitMessage.getPrice();
        if(price == null){
            return;
        }
        Integer liveTime = livePayRabbitMessage.getLiveTime();
        Integer liveMode = livePayRabbitMessage.getLiveMode();
        if(liveMode == 1){//计时收费
            price = price.multiply(BigDecimal.valueOf(liveTime));
        }
        //观看者
        Long userId = livePayRabbitMessage.getUserId();
        //主播
        Long linkUid = livePayRabbitMessage.getLiveUserId();
        User user = userService.getById(userId);
        User shower = userService.getById(linkUid);


        String agreeKey = RedisConstants.USER_SEND_GIFT +linkUid;
        boolean locked = redissonUtil.lock(agreeKey, 60);
        if (!locked) {
            return;
        }
        try {
            //观看者钱包余额
            BigDecimal dbMoney = this.findByUserId(userId);
            BigDecimal prices = BigDecimal.ZERO;

            if(dbMoney.compareTo(price) >= 0) {//钱包余额充足
                /**
                 * 用户
                 */
                //减少赠送者钱包余额
                this.subByUserId(userId, price);
                BigDecimal subtract = dbMoney.subtract(price);
                //修改消费总值
                baseMapper.addGiveAmount(userId, price);
                if(liveMode == 0){//常规收费
                    //新增钱包记录
                    String userRemark = "观看"+shower.getName()+"的直播";
                    this.genLivePayWalletRecord(price,livePayRabbitMessage, liveMode == 1?4:3, 1, subtract, userRemark, userId, linkUid);
                }


                 /**
                 * 主播
                 */
                BigDecimal rateMoney = BigDecimal.ZERO;
                Long belongAgent = shower.getBelongAgent();
                if(belongAgent != 0){

                    //TODO 平台提成 并添加记录
                    Agent agent = agentService.getById(belongAgent);
                    //平台提成金额
                    BigDecimal adminMoney = price.multiply(agent.getAdminRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                    //平台提成后剩余金额
                    price = price.subtract(adminMoney);
                    //发送平台提成mq消息
                    genAdminWalletRecord(belongAgent, adminMoney, user.getName(), shower.getName(), livePayRabbitMessage);

                    //TODO 代理商提成
                    //代理提成金额
                    BigDecimal rate = shower.getAgentRate().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN);
                    log.info("代理商提成比例=【{}】",shower.getAgentRate());
                    //log.info("计算后代理商提成比例=【{}】",rate);
                    rateMoney = price.multiply(rate);


                    //发送代理商提成MQ消息 只增加金额
                    AgentMoneyRabbitMessage agentMoneyRabbitMessage = new AgentMoneyRabbitMessage();
                    agentMoneyRabbitMessage.setAgentId(belongAgent);
                    agentMoneyRabbitMessage.setRateMoney(price);
                    //agentMoneyRabbitMessage.setRateMoney(rateMoney);
                    agentMoneyRabbitMessage.setType(3);
                    agentMoneyRabbitMessage.setRemark( user.getName()+"观看"+shower.getName()+"的直播");
                    agentMoneyRabbitMessage.setData(JSONObject.toJSONString(livePayRabbitMessage));
                    agentMoneyRabbitMessage.setMessageType(1);
                    agentMoneyRabbitMessage.setServiceType(1);
                    agentWalletRecordService.agentMoneyService(agentMoneyRabbitMessage);

                    prices = price;
                    //提成后剩余金额
                    price = price.subtract(rateMoney);
                }

                //增加被赠送者钱包余额
                BigDecimal dbLinkMoney = this.findByUserId(linkUid);
//                this.addByUserId(linkUid, price);
                BigDecimal add = dbLinkMoney.add(prices);
                //修改合计收到打赏（西施币）
                baseMapper.addGiftAmount(linkUid, prices);
                //if(liveMode == 0) {//常规收费

                    if(belongAgent != 0) {

                        //TODO 平台提成 并添加记录
                       /* Agent agent = agentService.getById(belongAgent);
                        //平台提成金额
                        BigDecimal adminMoney = price.multiply(agent.getAdminRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                        //平台提成后剩余金额
                        price = price.subtract(adminMoney);
                        //发送平台提成mq消息
                        genAdminWalletRecord(belongAgent, adminMoney, user.getName(), shower.getName(), livePayRabbitMessage);*/

                        //TODO 代理商增加记录
                        //发送代理商提成MQ消息 只增加记录
                        AgentMoneyRabbitMessage agentMoneyRabbitMessage = new AgentMoneyRabbitMessage();
                        agentMoneyRabbitMessage.setAgentId(belongAgent);
                        agentMoneyRabbitMessage.setRateMoney(prices);
                        //agentMoneyRabbitMessage.setRateMoney(rateMoney);
                        agentMoneyRabbitMessage.setType(3);
                        agentMoneyRabbitMessage.setServiceType(1);
                        agentMoneyRabbitMessage.setRemark( user.getName()+"观看"+shower.getName()+"的直播");
                        agentMoneyRabbitMessage.setData(JSONObject.toJSONString(livePayRabbitMessage));
                        agentMoneyRabbitMessage.setMessageType(2);
                        agentWalletRecordService.agentMoneyService(agentMoneyRabbitMessage);
                    }
                //}

                if(belongAgent == 0){
                    //TODO 平台提成 并添加记录
                   // Agent agent = agentService.getById(belongAgent);
                    //平台提成金额
                    BigDecimal adminMoney = price.multiply(user.getAgentRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                    //平台提成后剩余金额
                    price = price.subtract(adminMoney);
                    //发送平台提成mq消息
                    genAdminWalletRecord(belongAgent, adminMoney, user.getName(), shower.getName(), livePayRabbitMessage);
                }

                //新增钱包记录
                String showerRemark = user.getName()+"观看直播";
                this.genLivePayWalletRecord(price, livePayRabbitMessage, liveMode == 1 ? 4 : 3, 0, add, showerRemark, linkUid, userId);

                if(liveMode == 1){//计时收费
                    //新增钱包记录
                    String userRemark = "观看"+shower.getName()+"的直播";
                    this.genLivePayWalletRecord(price,livePayRabbitMessage, liveMode == 1?4:3, 1, subtract, userRemark, userId, linkUid);
                }
            }
        }finally {
            redissonUtil.unlock(agreeKey);
        }

        //发送用户等级MQ消息
        mqUserSender.sendAddUserGradeMessage(new UserRabbitMessage(MqUserServiceTypeConstens.USER_GRADE,price,userId));

        //发送直播等级MQ消息
        mqUserSender.sendAddLiveGradeMessage(new UserRabbitMessage(MqUserServiceTypeConstens.LIVE_GRADE,price,linkUid));
    }

    @Override
    @Transactional
    public void sendLivePayTimeCountRecord(LivePayRabbitMessage livePayRabbitMessage) {
        String timeCountKey = RedisConstants.LIVE_USER_TIME_COUNT+livePayRabbitMessage.getLiveRecordId()+livePayRabbitMessage.getUserId();
        Object o = redisService.get(timeCountKey);
        int timeCount = 1;
        if(o != null){
            timeCount = (int)o;
        }
        log.info("添加用户观看直播记录：用户【{}】观看直播分钟数是【{}】",livePayRabbitMessage.getUserId(),timeCount);
        //单价
        BigDecimal price = livePayRabbitMessage.getPrice();
        Integer liveTime = timeCount;
        livePayRabbitMessage.setLiveTime(timeCount);
        price = price.multiply(BigDecimal.valueOf(liveTime));
        //观看者
        Long userId = livePayRabbitMessage.getUserId();
        //主播
        Long linkUid = livePayRabbitMessage.getLiveUserId();

        User user = userService.getById(userId);
        User shower = userService.getById(linkUid);


        //观看者钱包余额
        BigDecimal dbMoney = this.findByUserId(userId);
       /**
         * 用户
         */
        String userRemark = "观看"+shower.getName()+"的直播";
        this.genLivePayWalletRecord(price,livePayRabbitMessage, 4, 1, dbMoney, userRemark, userId, linkUid);

        /**
         * 主播
         */
        Long belongAgent = shower.getBelongAgent();
        if(belongAgent != 0) {
            //TODO 平台提成 并添加记录
            Agent agent = agentService.getById(belongAgent);
            //平台提成金额
            BigDecimal adminMoney = price.multiply(agent.getAdminRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
            //平台提成后剩余金额
            price = price.subtract(adminMoney);
            //发送平台提成mq消息
            genAdminWalletRecord(belongAgent, price,user.getName(), shower.getName(), livePayRabbitMessage);


            //TODO 代理商提成
            //代理提成金额
            BigDecimal rateMoney = adminMoney.multiply(shower.getAgentRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));

            //TODO 代理商增加记录
            //发送代理商提成MQ消息 只增加记录
            AgentMoneyRabbitMessage agentMoneyRabbitMessage = new AgentMoneyRabbitMessage();
            agentMoneyRabbitMessage.setAgentId(belongAgent);
            agentMoneyRabbitMessage.setRateMoney(price);
            //agentMoneyRabbitMessage.setRateMoney(rateMoney);
            agentMoneyRabbitMessage.setType(4);
            agentMoneyRabbitMessage.setServiceType(1);
            agentMoneyRabbitMessage.setRemark( user.getName()+"观看"+shower.getName()+"的直播");
            agentMoneyRabbitMessage.setData(JSONObject.toJSONString(livePayRabbitMessage));
            agentMoneyRabbitMessage.setMessageType(2);
            agentWalletRecordService.agentMoneyService(agentMoneyRabbitMessage);

            //代理商提成后剩余金额
            price = price.subtract(rateMoney);
        }

        if(belongAgent == 0){
            //TODO 平台提成 并添加记录
            //Agent agent = agentService.getById(belongAgent);
            //平台提成金额
            BigDecimal adminMoney = price.multiply(user.getAgentRate().divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
            //平台提成后剩余金额
            price = price.subtract(adminMoney);
            //发送平台提成mq消息
            genAdminWalletRecord(belongAgent, adminMoney,user.getName(), shower.getName(), livePayRabbitMessage);

        }

        BigDecimal dbLinkMoney = this.findByUserId(linkUid);
        String showerRemark = user.getName()+"观看直播";
        this.genLivePayWalletRecord(price, livePayRabbitMessage, 4, 0, dbLinkMoney, showerRemark, linkUid, userId);

    }

    private void genAdminWalletRecord(Long belongAgent, BigDecimal rateMoney, String userName, String showerName, LivePayRabbitMessage livePayRabbitMessage){
        AdminMoneyRabbitMessage adminMoneyRabbitMessage = new AdminMoneyRabbitMessage();
        adminMoneyRabbitMessage.setUserId(belongAgent);
        //agentMoneyRabbitMessage.setRateMoney(price);
        adminMoneyRabbitMessage.setRateMoney(rateMoney);
        adminMoneyRabbitMessage.setType(4);
        adminMoneyRabbitMessage.setServiceType(1);
        adminMoneyRabbitMessage.setRemark( userName+"观看"+showerName+"的直播");
        adminMoneyRabbitMessage.setData(JSONObject.toJSONString(livePayRabbitMessage));
        adminMoneyRabbitMessage.setMessageType(2);
        adminWalletRecordService.adminMoneyService(adminMoneyRabbitMessage);
    }



    @Override
    @Transactional
    public void sendMoviePay(MoviePayRabbitMessage moviePayRabbitMessage) {
        BigDecimal price = moviePayRabbitMessage.getPrice();
        Long userId = moviePayRabbitMessage.getUserId();
        //观看者钱包余额
        BigDecimal dbMoney = this.findByUserId(userId);
        Long movieId = moviePayRabbitMessage.getMovieId();
        if(dbMoney.compareTo(price) >= 0) {//钱包余额充足
            /**
             * 用户
             */
            //减少赠送者钱包余额
            this.subByUserId(userId, price);
            BigDecimal subtract = dbMoney.subtract(price);
            //修改消费总值
            baseMapper.addGiveAmount(userId, price);
            //新增钱包记录
            String remark = "观看电影[" + moviePayRabbitMessage.getName() + "]";
            this.genMoviePayWalletRecord(moviePayRabbitMessage, subtract, remark, userId);
            //发送用户等级MQ消息
            mqUserSender.sendAddUserGradeMessage(new UserRabbitMessage(MqUserServiceTypeConstens.USER_GRADE,price,userId));
            //加入付费缓存 证明已付费
            redisService.set(RedisConstants.USER_PAY_MOVIE+userId+movieId,movieId);
        }
    }

    @Override
    public Boolean isUserWallet(Long id) {
        UserWallet userWallet = userWalletService.findWalletByUserId(id);
        BigDecimal giftAmount = userWallet.getGiftAmount();
        if(giftAmount.compareTo(BigDecimal.ZERO) > 0){
            return true;
        }
        return false;
    }

    private void genMoviePayWalletRecord(MoviePayRabbitMessage moviePayRabbitMessage, BigDecimal subtract, String remark, Long userId) {
        UserWalletRecord insert = new UserWalletRecord();
        insert.setType(5);
        insert.setUseType(1);
        insert.setUserId(userId);
        insert.setAmount(moviePayRabbitMessage.getPrice());
        insert.setCustNum(1);
        insert.setCustId(moviePayRabbitMessage.getMovieId());
        insert.setCustName(moviePayRabbitMessage.getName());
        insert.setRemark(remark);
        insert.setWalletAmount(subtract);
        userWalletRecordService.save(insert);
    }

    public void genWalletRecord(LiveGiftRabbitMessage liveGiftRabbitMessage,Integer type,Integer useType,BigDecimal walletMoney,String remark,Long userId,Long linkUId){
        BigDecimal giftMoney = liveGiftRabbitMessage.getGiftMoney();
        UserWalletRecord insert = new UserWalletRecord();
        insert.setType(type);
        insert.setUseType(useType);
        insert.setUserId(userId);
        insert.setAmount(giftMoney);
        insert.setLinkUid(linkUId);
        insert.setCustNum(liveGiftRabbitMessage.getNum());
        insert.setCustId(liveGiftRabbitMessage.getId());
        insert.setCustName(liveGiftRabbitMessage.getGiftName());
        insert.setRemark(remark);
        insert.setWalletAmount(walletMoney);
        insert.setLiveRecordId(liveGiftRabbitMessage.getLiveRecordId());
        userWalletRecordService.save(insert);
    }

    public void genLivePayWalletRecord(BigDecimal price,LivePayRabbitMessage livePayRabbitMessage,Integer type,Integer useType,BigDecimal walletMoney,String remark,Long userId,Long linkUId){
        UserWalletRecord insert = new UserWalletRecord();
        insert.setType(type);
        insert.setUseType(useType);
        insert.setUserId(userId);
        insert.setAmount(price);
        insert.setLinkUid(linkUId);
        insert.setCustNum(livePayRabbitMessage.getLiveTime());
        insert.setCustId(livePayRabbitMessage.getLiveId());
        insert.setCustName("");
        insert.setRemark(remark);
        insert.setWalletAmount(walletMoney);
        insert.setLiveRecordId(livePayRabbitMessage.getLiveRecordId());
        userWalletRecordService.save(insert);
    }

}
