package com.lipeng.spike.service.mapper;

import com.lipeng.spike.service.mapper.entity.SeckillEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SeckillMapper {

    @Select("SELECT SECKILLID AS seckillId, NAME as name, INVENTORY as inventory, START_TIME as startTime, END_TIME as endTime, CREATE_TIME as createTime, VERSION as version from meite_seckill where SECKILLID = #{seckillId}")
    SeckillEntity findBySeckillId(Long seckillId);

    @Update("update meite_seckill set INVENTORY = INVENTORY - 1, VERSION = VERSION + 1 where SECKILLID = #{seckillId} and INVENTORY > 0 and VERSION = #{version}")
    int inventoryDeduction(@Param("seckillId") Long seckillId, @Param("version") Long version);

}