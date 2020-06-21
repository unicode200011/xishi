package com.xishi.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lx
 */
@Data
@AllArgsConstructor
public class MailVo {
    private String name;
    private String dxNum;
    private String liveName;
    private String image;
    private Integer score;
    private String time;
    private String type;
}
