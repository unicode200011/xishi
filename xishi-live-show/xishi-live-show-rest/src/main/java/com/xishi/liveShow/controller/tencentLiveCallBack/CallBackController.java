package com.xishi.liveShow.controller.tencentLiveCallBack;

import com.alibaba.fastjson.JSONObject;
import com.common.base.util.MapUtil;
import com.xishi.liveShow.service.ILiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/callBack")
public class CallBackController {

    @Autowired
    private ILiveService liveService;

    /**
     * 推流回调
     * @param data
     * @return
     */
    @RequestMapping("/pushBack")
    public Map pushBack(@RequestBody TencentCallBackInfo data){
        log.info("推流回调,data=【{}】", JSONObject.toJSONString(data));
        if(data.getEvent_type() == 1){ //推流事件
            liveService.pushBack(data.getStream_id());
        }
        return MapUtil.build().put("code",0).over();
    }
    /**
     * 断流回调
     * @param data
     * @return
     */
    @RequestMapping("/discBack")
    public Map discBack(@RequestBody TencentCallBackInfo data){
        log.info("断流回调,data=【{}】", JSONObject.toJSONString(data));
        if(data.getEvent_type() == 0){ //断流事件
            liveService.discBack(data.getStream_id());
        }
        return MapUtil.build().put("code",0).over();
    }
    /**
     * 录制回调
     * @param data
     * @return
     */
    @RequestMapping("/shotBack")
    public Map shotBack(@RequestBody TencentCallBackInfo data){
        log.info("录制回调,data=【{}】", JSONObject.toJSONString(data));
        return MapUtil.build().put("code",0).over();
    }
    /**
     * 截图回调
     * @param data
     * @return
     */
    @RequestMapping("/cutBack")
    public Map cutBack(@RequestBody TencentCallBackInfo data){
        log.info("截图回调,data=【{}】", JSONObject.toJSONString(data));
        return MapUtil.build().put("code",0).over();
    }

    /**
     * 鉴黄回调
     * @param data
     * @return
     */
    @RequestMapping("/checkBack")
    public Map checkBack(@RequestBody TencentCallBackInfo data){
        log.info("鉴黄回调,data=【{}】", JSONObject.toJSONString(data));
        return MapUtil.build().put("code",0).over();
    }


}
