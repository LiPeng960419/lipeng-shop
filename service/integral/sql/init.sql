DROP TABLE IF EXISTS `meite_integral`;
CREATE TABLE `meite_integral`
(
  `ID`           int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_ID`      int(11)      DEFAULT NULL COMMENT '用户ID',
  `PAYMENT_ID`   varchar(128) DEFAULT NULL COMMENT '支付ID',
  `INTEGRAL`     bigint(20)   DEFAULT NULL COMMENT '积分',
  `AVAILABILITY` int(11)      DEFAULT NULL COMMENT '是否可用',
  `REVISION`     int(11)      DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY`   varchar(32)  DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime     DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY`   varchar(32)  DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='会员积分表';