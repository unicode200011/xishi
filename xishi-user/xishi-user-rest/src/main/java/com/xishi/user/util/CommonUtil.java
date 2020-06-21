package com.xishi.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.webcore.service.RedisService;
import com.cloud.webcore.util.SpringContextHolder;
import com.xishi.user.entity.constant.SystemConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * rest 工具类
 *
 * @author: lx
 */
public class CommonUtil {

    /**
     * 对象 2 Json 2 byte 数组
     *
     * @param object
     * @return
     */
    public static byte[] objectToByteArray(Object object) {
        return JSON.toJSONString(object).getBytes();
    }

    /**
     * byte 数组 2 对象
     *
     * @param byteArray
     * @param clz
     * @return
     */
    public static Object byteArrayToObject(byte[] byteArray, Class clz) {
        return JSONObject.parseObject(new String(byteArray), clz);
    }

    /**
     * 获取封面宽高
     *
     * @param url
     * @return
     */
    public static int[] getWidthAndHeight(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedImage sourceImg = ImageIO.read(is);
            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();
            return new int[]{width, height};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{720, 1280};
    }

    /**
     * 获取视频封面宽高
     *
     * @param url
     * @return
     */
    public static int[] getVideoCoverWidthAndHeight(Long videoId, String url) {
        String key = SystemConstants.VIDEO_COVER_DATA_KEY + videoId;
        RedisService redisService = SpringContextHolder.getBean(RedisService.class);
        Object cacheKg = redisService.getFromCache(key);
        if (cacheKg != null) {
            String kgStr = cacheKg.toString();
            String[] kgArr = kgStr.split("-");
            return new int[]{Integer.valueOf(kgArr[0]), Integer.valueOf(kgArr[1])};
        } else {
            int[] widthAndHeight = getWidthAndHeight(url);
            String kgStr = widthAndHeight[0] + "-" + widthAndHeight[1];
            redisService.toCache(key, kgStr);
            return widthAndHeight;
        }
    }
}
