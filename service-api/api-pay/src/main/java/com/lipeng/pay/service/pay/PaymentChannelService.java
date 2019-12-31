package com.lipeng.pay.service.pay;

import com.lipeng.pay.dto.PaymentChannelDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface PaymentChannelService {

    /**
     * 查询所有支付渠道
     */
    @GetMapping("/selectAll")
    List<PaymentChannelDTO> selectAll();

}