package com.xishi.user.entity.vo;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MoonCreateVo {
    private Long moonId;
    private Long moonApplyId;
    private String image;
    private String moonName;
    private String moonSymbol;
    private Long userId;
    private String address;
}
