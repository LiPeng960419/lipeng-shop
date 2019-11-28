package com.lipeng.spike.service.mapper;

import com.lipeng.spike.service.mapper.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper {

    @Insert("INSERT INTO `meite_order` VALUES (#{seckillId},#{userPhone},#{state}, now())")
    int insertOrder(OrderEntity orderEntity);

    @Select("SELECT SECKILLID AS seckillId, USERPHONE as userPhone, STATE as state, CREATE_TIME as createTime FROM meite_order WHERE USERPHONE = #{phone}  and SECKILLID = #{seckillId} AND STATE = 1")
    OrderEntity findByOrder(@Param("phone") String phone, @Param("seckillId") Long seckillId);

}