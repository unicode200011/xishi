package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import lombok.Data;

@Data
public class CommonQuery extends BaseQuery {

    private Integer type;

    private Long linkId;

    private Long linkUid;

    private String keyword;

    private String cover;

    private String[] kwArr;
    /**
     * 拓展字段
     */
    private String extra;

    private Integer sellType;
}
