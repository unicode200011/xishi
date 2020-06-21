package com.xishi.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class VideoWeightTransfer implements Serializable {
    private Long videoId;
    private Long userId;
}
