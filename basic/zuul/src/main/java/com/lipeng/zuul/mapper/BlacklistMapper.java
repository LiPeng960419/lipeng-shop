package com.lipeng.zuul.mapper;

import com.lipeng.zuul.mapper.entity.MeiteBlacklist;
import org.apache.ibatis.annotations.Select;

public interface BlacklistMapper {

	@Select("select ID AS id, IPADDRESS AS ipAddress, RESTRICTION_TYPE as restrictionType, AVAILABILITY as availability from meite_blacklist where IPADDRESS = #{ipAddress} and RESTRICTION_TYPE= 1")
	MeiteBlacklist findBlacklist(String ipAddress);

}