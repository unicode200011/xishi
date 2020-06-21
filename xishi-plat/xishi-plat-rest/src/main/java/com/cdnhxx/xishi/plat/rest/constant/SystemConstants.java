package com.cdnhxx.xishi.plat.rest.constant;

/**
 * 系统常量
 */
public class SystemConstants {
    /**
     * 公共数据缓存 三天过期
     */
    public static final String COMMEN_DATA_REDIS_KEY = "xishi:commonData:key:";
    public static final Long COMMEN_DATA_REDIS_KEY_EXPIRE_TIME = 60 * 60 * 24 * 3 * 1000L;

    /**
     * 1k 大小 1024 byte
     */
    public static final long KB_SIZE = 1024L;

    /**
     * 用于各种参数加解密的aes秘钥 需要使用 com.stylefeng.guns.rest.core.util.AesUtil 类来加解密
     */
    public static final String PARAM_ENC_AES_KEY = "7c73906645ba9bd6345a0d89695f83c0";
}
