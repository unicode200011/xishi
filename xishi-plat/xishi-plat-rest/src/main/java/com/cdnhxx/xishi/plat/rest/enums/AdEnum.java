package com.cdnhxx.xishi.plat.rest.enums;

import lombok.Getter;

/**
 * 广告枚举
 *
 * @author: lx
 */
@Getter
public enum AdEnum {
    StartPage("启动页广告", 0),
    IndexTop("首页顶部广告", 1),
    IndexBottom("首页底部广告", 1),
    Company("陪伴广告", 2),;

    private String msg;

    private int code;

    AdEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static AdEnum getByCode(int code) {
        for (AdEnum adEnum : values()) {
            if (adEnum.getCode() == code) {
                return adEnum;
            }
        }

        return null;
    }
}
