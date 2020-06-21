package com.xishi.user.entity.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author LX
 * @since 2020-02-20
 */
@Data
public class WatchRecordReq implements Serializable {


    private Long userId;

    private Long liveRecordId;

    private Integer state;

    private Date endTime;


}
