package com.lipeng.goods.service;


import com.lipeng.base.BaseResponse;
import com.lipeng.goods.dto.ProductDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface ProductSearchService {

    @GetMapping("/search")
    BaseResponse<List<ProductDto>> search(String name);

}