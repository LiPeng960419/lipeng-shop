package com.lipeng.pay.service.pay;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/15 10:17
 */
@Controller
public interface AliF2FPayCallBackSyncService {

    @RequestMapping("/synF2FCallBack")
    String synF2FCallBack(@RequestParam Map<String, String> map);

}