package com.lipeng.spike;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpikeController {

    /**
     * 秒杀商品详情页面
     */
    @RequestMapping("/details/{id}")
    public String details(@PathVariable("id") Long id) {
        return "details";
    }

}