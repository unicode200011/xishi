package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.req.AttentionQuery;
import com.xishi.user.entity.req.AttentionReq;
import com.xishi.user.entity.req.DynamicQuery;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.entity.vo.FansUserVo;
import com.xishi.user.model.pojo.Attention;
import com.xishi.user.dao.mapper.AttentionMapper;
import com.xishi.user.model.pojo.Dynamic;
import com.xishi.user.model.pojo.User;
import com.xishi.user.service.IAttentionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IDynamicService;
import com.xishi.user.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 关注 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-28
 */
@Service
public class AttentionServiceImpl extends ServiceImpl<AttentionMapper, Attention> implements IAttentionService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDynamicService dynamicService;

    @Override
    public Resp<List<FansUserVo>> getFansList(AttentionQuery data) {
        QueryWrapper<Attention> wrapper = new QueryWrapper<Attention>().eq("type", 0);
        Integer keyWord = data.getKeyWord();
        if(keyWord == 0){//关注列表
            wrapper.eq("user_id",data.getUserId());
        }else {//粉丝
            wrapper.eq("link_id",data.getUserId());
        }
        PageHelper.startPage(data.getPage(),data.getRows());
        List<Attention> list = this.list(wrapper);
        List<FansUserVo> fansUserVos = new ArrayList<>();
        for (Attention attention : list) {
            if(keyWord == 0){//关注列表
                Long linkId = attention.getLinkId();
                User byId = userService.getById(linkId);
                FansUserVo fansUserVo = TransUtil.transEntity(byId, FansUserVo.class);
                fansUserVo.setUserId(linkId);
                fansUserVos.add(fansUserVo);
            }else {//粉丝
                Long userId = attention.getUserId();
                User byId = userService.getById(userId);
                FansUserVo fansUserVo = TransUtil.transEntity(byId, FansUserVo.class);
                fansUserVo.setUserId(userId);
                int count = this.count(new QueryWrapper<Attention>().eq("user_id", data.getUserId()).eq("link_id", attention.getUserId()));
                fansUserVo.setAttention(count>0?1:0);
                fansUserVos.add(fansUserVo);
            }
        }
        return Resp.successData(fansUserVos);
    }

    @Override
    public Resp<List<DynamicVo>> getAttentionDynamicList(DynamicQuery data) {
        Long userId = data.getUserId();
        //查询关注的人
        List<Attention> attentions = this.list(new QueryWrapper<Attention>().eq("user_id", userId));
        if(CollectionUtils.isNotEmpty(attentions)){
            List<Long> linkIds = attentions.stream().map(Attention::getLinkId).collect(Collectors.toList());
            PageHelper.startPage(data.getPage(),data.getRows());
            List<Dynamic> dynamics = dynamicService.list(new QueryWrapper<Dynamic>().in("user_id", linkIds).orderByDesc("id").eq("state",0));
            List<DynamicVo> dynamicVos = TransUtil.transList(dynamics, DynamicVo.class);
            dynamicService.genDynamicVo(dynamicVos,userId);
            return Resp.successData(dynamicVos);
        }else {
            List<DynamicVo> dynamicVos = new ArrayList<>();
            return Resp.successData(dynamicVos);
        }

    }

    @Override
    public Resp<Void> submitAttention(AttentionReq data) {
        Long linkUserId = data.getLinkUserId();
        Integer type = data.getType();
        Attention count = this.getOne(new QueryWrapper<Attention>().eq("user_id", data.getUserId()).eq("link_id", linkUserId).eq("type",0));
        if(type == 0){//添加关注
            if(count != null){
                return Resp.error("已经添加关注");
            }
            Attention attention = new Attention();
            attention.setLinkId(linkUserId);
            attention.setUserId(data.getUserId());
            attention.setType(0);
            this.save(attention);
            //更新用户关注数
            userService.changeAttentionNum(data.getUserId(),0);//增加
            //更新被关注用户粉丝数
            userService.changeFansNum(linkUserId,0);
        }else {//取消关注
            if(count==null){
                return Resp.error("您未关注该用户");
            }
            this.removeById(count);
            //更新用户关注数
            userService.changeAttentionNum(data.getUserId(),1);//减少
            //更新被关注用户粉丝数
            userService.changeFansNum(linkUserId,1);
        }
        return Resp.success();
    }

}
