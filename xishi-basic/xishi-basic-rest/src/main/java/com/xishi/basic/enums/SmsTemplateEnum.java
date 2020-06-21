package com.xishi.basic.enums;

//短信模板枚举
public enum SmsTemplateEnum {

    VERIFY_CODE(426292,"验证码");

    private Integer templateId;

    private String name;

    private SmsTemplateEnum(Integer templateId,String name) {
        this.templateId=templateId;
        this.name=name;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public String getName() {
        return name;
    }
}
