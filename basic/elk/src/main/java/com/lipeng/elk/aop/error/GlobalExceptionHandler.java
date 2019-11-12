package com.lipeng.elk.aop.error;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.elk.kafka.KafkaSender;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private KafkaSender<JSONObject> kafkaSender;

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JSONObject exceptionHandler(Exception e) {
        log.error("###全局捕获异常###,error:{}", e);

        // 1.封装异常日志信息
        JSONObject errorJson = new JSONObject();
        JSONObject logJson = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        logJson.put("request_time", df.format(new Date()));
        logJson.put("error_info", e);

        errorJson.put("request_error", logJson);
        kafkaSender.send(errorJson);
        // 2. 返回错误信息
        JSONObject result = new JSONObject();
        result.put("code", 500);
        result.put("msg", "系统错误");

        // 参数中记录下，IP和端口号
        return result;
    }
}
