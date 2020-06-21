package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.base.component.qr.ImgQrTool;
import com.common.base.util.ToolUtil;
import com.component.cos.CosHelper;
import com.component.cos.CosResult;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.dao.mapper.InvitationCodeMapper;
import com.xishi.user.dao.mapper.InvitationUserMapper;
import com.xishi.user.model.pojo.InvitationCode;
import com.xishi.user.model.pojo.InvitationUser;
import com.xishi.user.service.IInvitationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
@Slf4j
public class InvitationCodeServiceImpl extends ServiceImpl<InvitationCodeMapper, InvitationCode> implements IInvitationCodeService {

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Autowired
    private InvitationUserMapper invitationUserMapper;

    @Value("${application.inviteRegUrl}")
    private String inviteRegUrl;

    @Autowired
    private CosHelper cosHelper;

    public String getInviteRegUrl() {
        return inviteRegUrl;
    }

    @Override
    public InvitationUser inviteUser(InvitationCode invitationCode, Long userId) {
        InvitationUser insert = new InvitationUser();
        insert.setCode(invitationCode.getCode());
        insert.setFromUid(invitationCode.getUserId());
        insert.setToUid(userId);
        insert.setCreateTime(new Date());
        invitationUserMapper.insert(insert);

        InvitationCode update = new InvitationCode();
        update.setId(invitationCode.getId());
        update.setUserNum(invitationCode.getUserNum() + 1);
        invitationCodeMapper.updateById(update);

        log.info("【邀请用户】插入邀请记录...邀请人ID=【{}】,使用邀请码=【{}】,受邀人ID=【{}】,此用户已邀请人数=【{}】", invitationCode.getUserId(), invitationCode.getCode(), userId, update.getUserNum());
        return insert;
    }

    @Override
    public boolean createInvitationCode(Long userId, String account) {
        String codeStr = getCode();
        InvitationCode code = new InvitationCode();
        code.setUserId(userId);
        code.setCode(codeStr);
        code.setCreateTime(new Date());
        //生成二维码
        String qrCode = this.genQrCode(userId,codeStr);
        code.setQrCode(qrCode);
        invitationCodeMapper.insert(code);
        log.info("【邀请码】创建邀请码成功...邀请码=【{}】,创建人ID=【{}】", codeStr, userId);
        return true;
    }

    //生成二维码
    private String genQrCode(Long userId,String code) {
        String conetent = inviteRegUrl+code;
        String jpgName = "qr"+userId+".jpg";

        String tmpDir = "/tmp/qr";
        String tmpPath = tmpDir+"/"+jpgName;
        String osName =System.getProperty("os.name");
        if(osName!=null && osName.toLowerCase().contains("window")) {
            tmpDir="d:\\tmp";
            tmpPath=tmpDir+"\\"+jpgName;
        }
        File dirFile = new File(tmpDir);
        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }

        ImgQrTool.createSimpleQr(conetent,200,200,tmpPath);
        File file = new File(tmpPath);
        if(!file.exists()) {
            ImgQrTool.createSimpleQr(conetent,200,200,tmpPath);
            file = new File(tmpPath);
        }
        String toPath = SystemConstants.QRCODE_PATH+"/"+(userId/1000);
        CosResult cosResult = cosHelper.uploadResource(toPath, file);
        if (!cosResult.isSuccess()) {
            cosResult=cosHelper.uploadResource(toPath, file);
        }
        file.delete();
        String qrCodeUrl = cosResult.getOrglUrl();
        log.info("qrCode url="+qrCodeUrl);
        return qrCodeUrl;
    }

    public String getCode() {
        while (true) {
            String code = ToolUtil.getRandomString(6).toUpperCase();
            InvitationCode invitationCode = this.queryByCode(code);
            if (invitationCode == null) {
                return code;
            }
        }
    }

    @Override
    public InvitationCode queryByCode(String code) {
        InvitationCode invitationCode = invitationCodeMapper.selectByCode(code);
        return invitationCode;
    }

    public InvitationCode queryByUser(Long userId) {
        InvitationCode invitationCode = invitationCodeMapper.selectOneByUserId(userId);
        return invitationCode;
    }

    public boolean genInviteQrCode(Long userId) {
        InvitationCode code = this.queryByUser(userId);
        String codeStr =code.getCode();
        String qr = code.getQrCode();
        if(StringUtils.isNotBlank(qr) && qr.endsWith(".jpg")) {
            return false;
        }
        //生成二维码
        String qrCode = this.genQrCode(userId,codeStr);
        InvitationCode codeUpt = new InvitationCode();
        codeUpt.setId(code.getId());
        codeUpt.setQrCode(qrCode);
        boolean rt =this.updateById(codeUpt);
        return rt;
    }
}
