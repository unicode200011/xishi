package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.github.pagehelper.PageHelper;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.req.DynamicQuery;
import com.xishi.user.entity.req.DynamicReq;
import com.xishi.user.entity.vo.DynamicCommentVo;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.model.pojo.Dynamic;
import com.xishi.user.dao.mapper.DynamicMapper;
import com.xishi.user.model.pojo.DynamicComment;
import com.xishi.user.model.pojo.User;
import com.xishi.user.service.IDynamicCommentService;
import com.xishi.user.service.IDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 动态列表 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {

    @Autowired
    RedisService redisService;

    @Autowired
    IUserService userService;

    @Autowired
    private IDynamicCommentService dynamicCommentService;

    @Override
    public Resp<Void> sendDynamic(DynamicReq data) {
        Dynamic dynamic = TransUtil.transEntity(data, Dynamic.class);
        boolean save = this.save(dynamic);
        return Resp.success();
    }

    @Override
    public Resp<Void> deleteDynamic(DynamicReq data) {
        Dynamic byId = this.getById(data.getId());
        if(byId == null){
            Resp.error("动态不存在");
        }
        byId.setState(1);//删除
        this.updateById(byId);
        return Resp.success();
    }

    @Override
    public Resp<Void> praiseDynamic(DynamicReq data) {
        Integer type = data.getType();
        Long id = data.getId();
        if(id == null || type == null){
            return Resp.error("参数错误");
        }
        if(type == 0){
            redisService.set(RedisConstants.USER_DYNAMIC_PRAISE+data.getUserId()+data.getId(),data.getId());
        }else {
            redisService.remove(RedisConstants.USER_DYNAMIC_PRAISE+data.getUserId()+data.getId());
        }
        baseMapper.changePraiseNum(id,type);
        userService.changePraiseNum(id,type);
        return Resp.success();
    }

    @Override
    public Resp<List<DynamicVo>> getDynamics(DynamicQuery data) {
        PageHelper.startPage(data.getPage(),data.getRows());
        QueryWrapper<Dynamic> wrapper = new QueryWrapper();
        wrapper.eq("user_id",data.getUserId());
        wrapper.eq("state",0);
        wrapper.orderByDesc("create_time");
        List<Dynamic> list = this.list(wrapper);
        List<DynamicVo> dynamicVos = TransUtil.transList(list, DynamicVo.class);

        for(int i=0; i<dynamicVos.size(); i++){
            DynamicVo dynamicVo = dynamicVos.get(i);
            if(DateUtils.formatDate(dynamicVo.getCreateTime(), "yyyy:MM:dd").equals(DateUtils.formatDate(new Date(), "yyyy:MM:dd"))){
                dynamicVo.setCreateTimeStr(DateUtils.formatDate(dynamicVo.getCreateTime(), "hh:MM"));
            }
        }

        //封装动态的评论
        this.genDynamicVo(dynamicVos,data.getUserId());
        return new Resp<>(dynamicVos);
    }

    @Override
    public void genDynamicVo(List<DynamicVo> dynamicVos,Long userId){
        for (DynamicVo dynamicVo : dynamicVos) {
            String str = DateUtils.parsePastTime(dynamicVo.getCreateTime());
            dynamicVo.setCreateTimeStr(str);
            //判断是否点赞
            boolean exists = redisService.exists(RedisConstants.USER_DYNAMIC_PRAISE + userId + dynamicVo.getId());
            dynamicVo.setIsPraise(exists?1:0);

            //查询动态评论列表
            List<DynamicComment> dynamicComments = dynamicCommentService.list(new QueryWrapper<DynamicComment>().eq("state", 0).eq("dynamic_id", dynamicVo.getId()).eq("parent_id",0).orderByDesc("create_time").last(" LIMIT 5 "));
            List<DynamicCommentVo> dynamicCommentVos = TransUtil.transList(dynamicComments, DynamicCommentVo.class);
            for (DynamicCommentVo dynamicCommentVo : dynamicCommentVos) {
                User byId = userService.getById(dynamicCommentVo.getUserId());
                if(byId  != null){
                    dynamicCommentVo.setAvatar(byId.getAvatar());
                    dynamicCommentVo.setUserName(byId.getName());
                    dynamicCommentVo.setCreateTimeStr(DateUtils.parsePastTime(dynamicCommentVo.getCreateTime()));
                }
            }
            //将评论组装
            dynamicVo.setCommentVos(dynamicCommentVos);
            //组装动态用户信息
            User byId = userService.getById(dynamicVo.getUserId());
            if(byId  != null){
                dynamicVo.setAvatar(byId.getAvatar());
                dynamicVo.setUserName(byId.getName());
            }
        }
    }

    @Override
    public void changeCommentNum(Long dynamicId,Integer type) {
        baseMapper.changeCommentNum(dynamicId,type);
    }
}
