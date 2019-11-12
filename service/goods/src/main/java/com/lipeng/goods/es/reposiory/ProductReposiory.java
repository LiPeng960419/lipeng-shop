package com.lipeng.goods.es.reposiory;

import com.lipeng.goods.es.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Integer> {

}
 