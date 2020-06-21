package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.*;
import com.common.base.util.CommonUtil;
import com.common.base.util.HttpKit;
import com.github.pagehelper.PageHelper;
import com.xishi.user.config.BBPayConfig;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.dao.mapper.*;
import com.xishi.user.entity.req.*;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.PayResponInfo;
import com.xishi.user.model.pojo.*;
import com.xishi.user.service.*;
import com.xishi.user.util.*;
import com.xishi.user.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Value("${application.inviteRegUrl}")
    private String inviteRegUrl;
    @Autowired
    private NationMapper nationMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private IUserAuthInfoService userAuthInfoService;
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;
    @Autowired
    private INationService nationService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IUserGradeService userGradeService;
    @Autowired
    private ILiveGradeService liveGradeService;
    @Autowired
    private IUserWalletService userWalletService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IAttentionService attentionService;
    @Autowired
    private BBPayConfig bbPayConfig;
    @Autowired
    private IPayService payService;
    @Autowired
    private IPayTypeService payTypeService;
    @Autowired
    private IPayAndTypeService payAndTypeService;


    public Resp<Void> checkUser(User user) {
        if(user == null){
            return new Resp<Void>(403,"用户不存在");
        }
        Integer state =user.getState();
        if(state!=null && state==1) {
            return new Resp<Void>(403,"用户冻结中");
        }
        return Resp.success();
    }


    @Override
    public void getTast(GetTaskReq gradeReq) {
    }

    @Override
    public Resp<Void> applyUserAuthInfo(AuthRequestVo data) {
        boolean codeCheck = smsService.checkCode(data.getPhone(), data.getCode());
        if (!codeCheck) {
            return Resp.error("验证码错误");
        }
        UserAuthInfo userAuthInfo = userAuthInfoService.getOne(new QueryWrapper<UserAuthInfo>().eq("user_id", data.getUserId()));
        if(userAuthInfo == null){
            if(StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getIdCard())){
                return Resp.error("参数错误");
            }
            UserAuthInfo insert = new UserAuthInfo();
            insert.setUserId(data.getUserId());
            insert.setIdCard(data.getIdCard());
            insert.setName(data.getName());
            userAuthInfoService.save(insert);
        }else {
            UserAuthInfo updateInfo = TransUtil.transEntity(data, UserAuthInfo.class);
            updateInfo.setId(userAuthInfo.getId());
            updateInfo.setState(0);
            userAuthInfoService.updateById(updateInfo);
        }
        //跟新用户状态
            User byId = this.getById(data.getUserId());
            byId.setApplyStatus(1);
            byId.setUpdateTime(new Date());
            this.updateById(byId);
        return Resp.success("申请成功");
    }

    @Override
    public Resp<UserAuthVo> getUserAuthInfo(Long userId) {
        UserAuthInfo userAuthInfo = userAuthInfoService.getOne(new QueryWrapper<UserAuthInfo>().eq("user_id", userId));
        if(userAuthInfo == null){
            Resp.error("实名信息不存在");
        }
        UserAuthVo userAuthVo = TransUtil.transEntity(userAuthInfo, UserAuthVo.class);
        return new Resp<>(userAuthVo);
    }

    @Override
    public Resp<UserAuthVo> updateUserAuthInfo(AuthRequestVo data) {
        boolean codeCheck = smsService.checkCode(data.getPhone(), data.getCode());
        if (!codeCheck) {
            return Resp.error("验证码错误");
        }
        UserAuthInfo userAuthInfo = userAuthInfoService.getOne(new QueryWrapper<UserAuthInfo>().eq("user_id", data.getUserId()));
        if(userAuthInfo == null){
            if(StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getIdCard())){
                return Resp.error("参数错误");
            }
            UserAuthInfo insert = new UserAuthInfo();
            insert.setUserId(data.getUserId());
            insert.setIdCard(data.getIdCard());
            insert.setName(data.getName());
            userAuthInfoService.save(insert);
        }else {
            UserAuthInfo updateInfo = TransUtil.transEntity(data, UserAuthInfo.class);
            updateInfo.setId(userAuthInfo.getId());
            userAuthInfoService.updateById(updateInfo);
        }
        UserAuthInfo newAuthInfo = userAuthInfoService.getOne(new QueryWrapper<UserAuthInfo>().eq("user_id", data.getUserId()));
        UserAuthVo userAuthVo = TransUtil.transEntity(newAuthInfo, UserAuthVo.class);
        return new Resp<>(userAuthVo);
    }

    @Override
    public boolean checkLevel(Long userId, Integer level) {

        User byId = this.getById(userId);
        Integer userLevel = byId.getUserLevel();
        Integer shower = byId.getShower();
        if(shower == 1 || userLevel >= level){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Resp<UserGradeDetailVo> getUserGradeDetail(Long userId) {
        User byId = this.getById(userId);
        UserGradeDetailVo userGradeDetailVo = new UserGradeDetailVo();
        userGradeDetailVo.setUserId(byId.getId());
        userGradeDetailVo.setUserLevel(byId.getUserLevel());
        userGradeDetailVo.setXishiNum(byId.getXishiNum());
        userGradeDetailVo.setAvatar(byId.getAvatar());
        //查询用户消费总值
        UserWallet walletByUserId = userWalletService.findWalletByUserId(userId);
        userGradeDetailVo.setExpNum(walletByUserId.getGiveAmount());

        //查询当前用户等级信息
        UserGrade level = userGradeService.getOne(new QueryWrapper<UserGrade>().eq("level", byId.getUserLevel()));
        userGradeDetailVo.setImage(level==null?"":level.getImage());
        userGradeDetailVo.setColor(level==null?"":level.getColor());
        userGradeDetailVo.setRgb(level==null?"":level.getRgb());
        //查询下一等级经验值
        UserGrade nextLevel = userGradeService.getOne(new QueryWrapper<UserGrade>().eq("level", byId.getUserLevel()+1));
        userGradeDetailVo.setMaxExpNum(nextLevel==null?level.getAmount():nextLevel.getAmount());
        //获取所有等级信息
        List<UserGrade> list = userGradeService.list();
        List<UserGradeVo> userGradeVos = TransUtil.transList(list, UserGradeVo.class);
        userGradeDetailVo.setUserGrades(userGradeVos);
        return Resp.successData(userGradeDetailVo);
    }

    @Override
    public Resp<UserGradeDetailVo> getUserLiveGradeDetail(Long userId) {
        User byId = this.getById(userId);
        UserGradeDetailVo userGradeDetailVo = new UserGradeDetailVo();
        userGradeDetailVo.setUserId(byId.getId());
        userGradeDetailVo.setUserLevel(byId.getLiveLevel());
        userGradeDetailVo.setXishiNum(byId.getXishiNum());
        userGradeDetailVo.setAvatar(byId.getAvatar());
        //查询主播接收总值
        UserWallet walletByUserId = userWalletService.findWalletByUserId(userId);
        userGradeDetailVo.setExpNum(walletByUserId.getGiftAmount());

        //查询当前直播等级信息
        LiveGrade level = liveGradeService.getOne(new QueryWrapper<LiveGrade>().eq("level", byId.getLiveLevel()));
        userGradeDetailVo.setImage(level==null?"":level.getImage());

        //获取用户等级
        UserGrade userGrade = userGradeService.getOne(new QueryWrapper<UserGrade>().eq("level", byId.getUserLevel()));
        userGradeDetailVo.setColor(userGrade==null?"":userGrade.getColor());
        userGradeDetailVo.setRgb(userGrade==null?"":userGrade.getRgb());

        //查询下一等级经验值
        LiveGrade nextLevel = liveGradeService.getOne(new QueryWrapper<LiveGrade>().eq("level", byId.getLiveLevel()+1));
        userGradeDetailVo.setMaxExpNum(nextLevel==null?level.getAmount():nextLevel.getAmount());
        //获取所有等级信息
        List<LiveGrade> list = liveGradeService.list();
        List<UserGradeVo> userGradeVos = TransUtil.transList(list, UserGradeVo.class);
        userGradeDetailVo.setUserGrades(userGradeVos);
        return Resp.successData(userGradeDetailVo);
    }

    @Override
    public void changePraiseNum(Long id, Integer type) {
        baseMapper.changePraiseNum(id,type);
    }

    @Override
    public void changeAttentionNum(Long id, Integer type) {
        baseMapper.changeAttentionNum(id,type);
    }

    @Override
    public void changeFansNum(Long id, Integer type) {
        baseMapper.changeFansNum(id,type);
    }

    @Override
    public UserInfoVo otherUserInfo(UserReq data) {
        UserInfoVo userInfoVo = this.userInfo(data.getLinkUserId());
        if(userInfoVo == null){
            return null;
        }
        int count = attentionService.count(new QueryWrapper<Attention>().eq("user_id", data.getUserId()).eq("link_id", data.getLinkUserId()));
        userInfoVo.setAttention(count>0?1:0);
        return userInfoVo;
    }

    @Override
    public Resp<Integer> checkAttention(UserAttentionQuery query) {
        int count = attentionService.count(new QueryWrapper<Attention>().eq("user_id", query.getUserId()).eq("link_id", query.getLinkUid()));
        return Resp.successData(count>0?1:0);
    }

    @Override
    public boolean registToBB(String xishiNo) {
        Map param = new HashMap();
        String nowTimeTro8 = DateUtils.getNowTimeTro8();
        param.put("MerCode",bbPayConfig.getMerCode());
        param.put("Timestamp",DateUtils.getTimeTro()+"");
        param.put("UserName",xishiNo);
        String paramStr = DateUtils.getHttpGetParam(false, bbPayConfig.getAddressUrl(), param);
        String B = MD5.GetMD5Code(bbPayConfig.getMerCode() + xishiNo + bbPayConfig.getKeyB() + nowTimeTro8);
        String key = "qazqwe"+B+"yuqw";

        //封装请求参数
        Map requst = new HashMap();
        DESUtil desUtil = new DESUtil(bbPayConfig.getDesKey());
        try {
            requst.put("param", desUtil.encrypt(paramStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requst.put("key",key);

        String respon = HttpKit.sendGet(bbPayConfig.getAddUserUrl(), requst);
        Map map = JSONObject.parseObject(respon, Map.class);
        Boolean success =(Boolean) map.get("Success");
        if (success) {
            log.info("xishiNum【{}】 regist to BB  success,", xishiNo);
            return true;
        }else {
            log.info("xishiNum【{}】 regist to BB  faild,reason【{}】", xishiNo,JSONObject.toJSONString(map));
            return false;
        }
    }

    @Override
    public boolean getAddressFromBB(String xishiNo,Integer type) {
        Map param = new HashMap();
        String nowTimeTro8 = DateUtils.getNowTimeTro8();
        param.put("MerCode",bbPayConfig.getMerCode());
        param.put("Timestamp",DateUtils.getTimeTro()+"");
        param.put("UserType",type+"");
        param.put("UserName",xishiNo);
        param.put("CoinCode",bbPayConfig.getCoinCode());
        String paramStr = DateUtils.getHttpGetParam(false, bbPayConfig.getAddressUrl(), param);
        String B = MD5.GetMD5Code(bbPayConfig.getMerCode() + type + bbPayConfig.getCoinCode()+bbPayConfig.getKeyB() + nowTimeTro8);
        String key = "qazqwe"+B+"yuqw";

        //封装请求参数
        Map requst = new HashMap();
        DESUtil desUtil = new DESUtil(bbPayConfig.getDesKey());
        try {
            requst.put("param", desUtil.encrypt(paramStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requst.put("key",key);

        String respon = HttpKit.sendGet(bbPayConfig.getAddressUrl(), requst);
        Map map = JSONObject.parseObject(respon, Map.class);
        Boolean success =(Boolean) map.get("Success");
        if (success) {
            Object data = map.get("Data");
            Map dataVal = (Map)data;
            log.info("xishiNum【{}】 get address from BB  success,", xishiNo);
            if(type == 1){ //TODO 币宝用户地址缓存
                redisService.set(RedisConstants.LIVE_USER_NO_BB+xishiNo,dataVal.get("Address"));
            }else {
                redisService.set(RedisConstants.LIVE_AGENT_NO_BB,dataVal.get("Address"));
            }
            return true;
        }else {
            log.info("xishiNum【{}】 get address from BB  faild,reason【{}】", xishiNo,JSONObject.toJSONString(map));
            return false;
        }
    }

    @Override
    public Resp<Map> getLoginUrlFromBB(BBLoginReq bbLoginReq) {
        Map param = new HashMap();
        String nowTimeTro8 = DateUtils.getNowTimeTro8();
        param.put("MerCode",bbPayConfig.getMerCode());
        param.put("Timestamp",DateUtils.getTimeTro()+"");
        param.put("Type",bbLoginReq.getType()+""); //请求类型 0-查看订单，1-买币，2-卖币
        param.put("UserName",bbLoginReq.getXishiNo());
        param.put("Amount",bbLoginReq.getAmount()+"");
        param.put("OrderNum",bbLoginReq.getOrderNum());
        param.put("PayMethods",bbLoginReq.getPayType() == 0?"aliPay":"weChatpay"); //:bankcard: 银行卡 ,aliPay: 支付宝 ,weChatpay: 微信,payPal:PayPal
        param.put("SkipOrderNo",bbLoginReq.getOrderNum());
        param.put("Coin",bbPayConfig.getCoinCode());
        String paramStr = DateUtils.getHttpGetParam(false, bbPayConfig.getLoginUrl(), param);

        String B = MD5.GetMD5Code(bbPayConfig.getMerCode()+bbLoginReq.getXishiNo() + bbLoginReq.getType() + bbLoginReq.getOrderNum()+bbPayConfig.getKeyB() + nowTimeTro8);
        String key = "qazqwe"+B+"qwyu";

        //封装请求参数
        Map requst = new HashMap();
        DESUtil desUtil = new DESUtil(bbPayConfig.getDesKey());
        try {
            requst.put("param", desUtil.encrypt(paramStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requst.put("key",key);

        String respon = HttpKit.sendGet(bbPayConfig.getLoginUrl(), requst);
        Map map = JSONObject.parseObject(respon, Map.class);
        Boolean success =(Boolean) map.get("Success");
        if (success) {
            log.info("xishiNum【{}】 getLoginUrlFromBB success,data=【{}】", bbLoginReq.getXishiNo(),map);
        }else {
            log.info("xishiNum【{}】getLoginUrlFromBB  faild,reason【{}】", bbLoginReq.getXishiNo(),JSONObject.toJSONString(map));
            return Resp.error(map.get("Message").toString());
        }
        return Resp.successData(map);
    }

    @Override
    public String getCommentLevel(Integer id) {
        return baseMapper.getCommentLevel(id);
    }

    @Override
    public Resp<Integer> isPayPwd(Long id) {
        User user = baseMapper.selectById(id);
        boolean b = StrKit.isNotEmpty(user.getPayPwd());
       // UserPayPwdReq userPayPwdVo = new UserPayPwdReq();
        //userPayPwdVo.setIsPayPwd(b == true ? 1 : 0);
        return Resp.successData(b == true ? 1 : 0);
    }


    public static void main(String[] args) {
        String clusterUrl = "https://api.i2605.com/jzpay_exapi/v1/order/createOrder";
//        String masterUrl = "https://api.jzpay.vip/jzpay_exapi/v1/order/createOrder";
//        String merchantId = "4a46acf719f9b7ecd926dbe9b5adf184";
//        String apiKey = "2b0a292d864829adeb6684da6a4c5807";

        String masterUrl = "https://api.zhbipay.vip/zbpay_exapi/v2/order/createOrder";
        String merchantId = "671f9c38ed717287fe4fb3252349c131";
        String apiKey = "ca547c4f7dc41dfd001730e4b8a65fcb";

        Map param = new HashMap();
        //param.put("notifyUrl","http://api.xishi.nhys.cdnhxx.com/users/pay/callback/bbPayCallback");
        param.put("notifyUrl","http://api.xishi88.live/users/pay/callback/bbPayCallback");
        param.put("jExtra","1");
        param.put("currency","CNY");
        param.put("amount","300.00");
        param.put("payWay","AliPay");
        param.put("orderType","1");
        param.put("jOrderId","2020020189768");
        param.put("jUserIp","65.18.114.105");
        param.put("jUserId","1");
        param.put("signatureVersion","1");
        param.put("signatureMethod","HmacSHA256");
        param.put("merchantId",merchantId);
        param.put("timestamp", System.currentTimeMillis()+"");

        String sign = JzSignUtil.createSign(param, apiKey);
        System.out.println("sign:【"+sign+"】");
        param.put("signature",sign);

        String paramStr = DateUtils.getHttpGetParam(false, "", param);
        System.out.println("paramStr:【"+paramStr+"】");

        String respon = HttpUtils.sendPost(masterUrl, paramStr);
        System.out.println("响应respon："+respon);
        PayResponInfo payResponInfo = JSONObject.parseObject(respon, PayResponInfo.class);
        System.out.println("payResponInfo："+JSONObject.toJSONString(payResponInfo));

    }

    public Resp<Object> getPayResponse(PayAndType payAndType,String ip,String orderNum,BigDecimal amount,String jExtra,String xishiNum){
        Integer typeId = payAndType.getTypeId();
        Integer payId = payAndType.getPayId();
        Pay pay = payService.getById(payId);
        PayType payType = payTypeService.getById(typeId);

        Map param = new HashMap();
        param.put("notifyUrl",pay.getNotifyUrl());
        param.put("jExtra",jExtra);
        param.put("currency","CNY");
        param.put("amount",amount.setScale(2,BigDecimal.ROUND_DOWN)+"");
        param.put("payWay",payType.getType());
        param.put("orderType","1");
        param.put("jOrderId",orderNum);
        param.put("jUserIp",ip);
        param.put("jUserId",xishiNum);
        param.put("signatureVersion","1");
        param.put("signatureMethod",pay.getEncodeType());
        param.put("merchantId",pay.getMerchantId());
        param.put("timestamp", System.currentTimeMillis()+"");

        String sign = JzSignUtil.createSign(param, pay.getApiKey());
        System.out.println("sign:【"+sign+"】");
        param.put("signature",sign);

        String paramStr = DateUtils.getHttpGetParam(false, "", param);
        System.out.println("paramStr:【"+paramStr+"】");

        String respon = HttpUtils.sendPost(pay.getCreateMusterUrl(), paramStr);
        System.out.println("响应respon："+respon);
        PayResponInfo payResponInfo = JSONObject.parseObject(respon, PayResponInfo.class);

        if(payResponInfo.getCode() != 0){
            return Resp.error(payResponInfo.getMessage());
        }
        return Resp.successData(payResponInfo);
    }


    @Override
    public User getFromCache(Long userId) {
        String userKey = SystemConstants.USER_REDIS_KEY + userId;
        Object userCache = redisService.getFromCache(userKey);
        if (userCache != null) {
            return (User) userCache;
        } else {
            User user = baseMapper.selectById(userId);
            if (user != null) {
                redisService.toCache(userKey, user);
            }
            return user;
        }
    }

    @Override
    public void reloadUserToCache(Long userId) {
        String userKey = SystemConstants.USER_REDIS_KEY + userId;
        User user = baseMapper.selectById(userId);
        if (user != null) {
            redisService.toCache(userKey, user);
        }
    }

    @Override
    public UserInfoVo userInfo(Long userId) {
        User dbUser = this.getById(userId);
        if(dbUser == null) return null;
        UserInfoVo userInfoVo = TransUtil.transEntity(dbUser, UserInfoVo.class);
        userInfoVo.setUserId(dbUser.getId());
        userInfoVo.setAddress(dbUser.getProvince() + "-" + dbUser.getCity() + "-" + dbUser.getCounty());
        Agent byId = agentService.getById(dbUser.getBelongAgent());
        userInfoVo.setBelongAgentName(byId==null?"":byId.getAgentName());
        //封装用户等级颜色
        UserGrade level = userGradeService.getOne(new QueryWrapper<UserGrade>().eq("level", userInfoVo.getUserLevel()));
        userInfoVo.setUserLevelColor(level.getRgb());
        if(dbUser.getShower() ==1){
            Integer state = baseMapper.selectLiveState(userId);
            LiveVo liveVo = baseMapper.selectLive(userId);
            if(liveVo == null){
                return null;
            }
            userInfoVo.setStreamName(liveVo.getStreamName());
            userInfoVo.setLivePrice(liveVo.getLivePrice());
            userInfoVo.setLivePwd(liveVo.getLivePwd());
            userInfoVo.setLiveId(liveVo.getLiveId());
            userInfoVo.setLiveState(state);
        }
        int hasPayPwd = 0;
        String payPwd = dbUser.getPayPwd();
        if (StringUtils.isNotBlank(payPwd)) {
            hasPayPwd = 1;
        }
        userInfoVo.setHasPayPwd(hasPayPwd);
        return userInfoVo;
    }

    @Autowired
    private IKeywordService keywordService;

    @Override
    public Resp<Void> updateUserInfo(User user) {
        Long userId = user.getId();
        User update = new User();
        update.setId(userId);
        update.setUpdateTime(new Date());
        update.setFirstLogin(0);
        //昵称
        if (StrKit.isNotEmpty(user.getName())) {
            update.setName(user.getName());
        }

        //头像
        if (StrKit.isNotEmpty(user.getAvatar())) {
            update.setAvatar(user.getAvatar());
        }
        //省份
            update.setProvince(user.getProvince());
        //城市
            update.setCity(user.getCity());

        //区县
            update.setCounty(user.getCounty());

        //个人简介
        if(StrKit.isNotEmpty(user.getIntro())){
            String s = keywordService.replaceWord(user.getIntro());
            update.setIntro(s);
        }

        //性别
        if (user.getGender() != null) update.setGender(user.getGender());

        //职业
        update.setJob(user.getJob());

        //生日
        if (user.getBirthday() != null) {
            Date birthday = user.getBirthday();
            int age = DateTimeKit.ageOfNow(birthday);
            update.setAge(age);
            update.setBirthday(birthday);
        }
        boolean result = updateById(update);
        reloadUserToCache(userId);
        return result ? Resp.success("修改成功") : Resp.error("修改失败,请稍后再试");
    }

    @Override
    public TuiGuangVo tuiGuangData(Long userId) {
        User user = getById(userId);
        InvitationCode invitationCode = invitationCodeMapper.selectOneByUserId(userId);
        String longUrl = inviteRegUrl + CommonUtil.encryptResult(userId.toString(), SystemConstants.SHARE_USER_PARAM_KEY);
        String shortUrl = LongUrlToShortUrl.conversion(longUrl);
        TuiGuangVo tuiGuangVo = new TuiGuangVo();
        tuiGuangVo.setAvatar(user.getAvatar());
        tuiGuangVo.setName(user.getName());
        tuiGuangVo.setUserId(user.getId());
        tuiGuangVo.setCode(invitationCode != null ? invitationCode.getCode() : "");
        tuiGuangVo.setUrl(shortUrl);
        return tuiGuangVo;
    }

    @Override
    public UserAuthVo userAuthInfo(Long userId) {
        // 0 待认证 1 认证中 2 已认证 3 认证失败
        int state = 0;
        UserAuthInfo userAuthInfo = userAuthInfoService.getOne(new QueryWrapper<UserAuthInfo>().eq("user_id",userId));
        UserAuthVo userAuthVo = TransUtil.transEntity(userAuthInfo, UserAuthVo.class);
        if (userAuthVo == null) {
            userAuthVo = new UserAuthVo();
            userAuthInfo.setState(state);
        }
        return userAuthVo;
    }

    public User queryByPhone(String phone) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        return user;
    }

    //身份认证通过
    public boolean identifyAuthPass(Long userId) {
        User userUpt = new User();
        userUpt.setId(userId);
        userUpt.setUpdateTime(new Date());
        this.updateById(userUpt);
        return true;
    }

    //搜索用户
    public List<UserInfoVo> queryUserList(UserSearchQuery query) {

        String keyword = query.getKeyword();
        QueryWrapper<User> wrapper = new QueryWrapper();
        if(StrKit.isNotEmpty(keyword)){
            wrapper.like("xishi_num",keyword);
            wrapper.or().like("name",keyword);
        }
        PageHelper.startPage(query.getPage(),query.getRows());
        List<User> users = baseMapper.selectList(wrapper);
        List<UserInfoVo> userInfoVos = TransUtil.transList(users, UserInfoVo.class);
        for (UserInfoVo userInfoVo : userInfoVos) {
            userInfoVo.setUserId(userInfoVo.getId());
            int count = attentionService.count(new QueryWrapper<Attention>().eq("user_id", query.getUserId()).eq("link_id", userInfoVo.getId()));
            userInfoVo.setAttention(count>0?1:0);
        }
        return userInfoVos;
    }

    //生成西施号
    public String genAccountNo() {
        String accountNo = ToolUtil.getRandomNum(9);
        User user = this.queryByNo(accountNo);
        while (user != null) {
            accountNo = ToolUtil.getRandomNum(9);
            user = this.queryByNo(accountNo);
        }
        return accountNo;
    }

    //根据西施号查询用户
    public User queryByNo(String accountNo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("xishi_num", accountNo);
        User user = baseMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public User queryByThirdAccount(String account, Integer thirdType) {
        return baseMapper.selectByThirdAccount(account, thirdType);
    }

    //修改等级
    public boolean changeGrade(ChangeGradeReq gradeReq) {
        Long userId = gradeReq.getUserId();
        Integer grade = gradeReq.getGrade();
        log.info("UserServiceImpl changeGrade start ,userId={},grade={}",userId,grade);
        User userUpt = new User();
        userUpt.setId(gradeReq.getUserId());
        userUpt.setUpdateTime(new Date());
        boolean rt = this.updateById(userUpt);
        return rt;
    }


}
