CREATE TABLE `meite_order`
(
  `SECKILLID`   bigint(20)  NOT NULL COMMENT '秒杀商品id',
  `USERPHONE`   varchar(11) NOT NULL COMMENT '用户手机号',
  `STATE`       tinyint(4)  NOT NULL DEFAULT '-1' COMMENT '状态标示:-1:无效 0:成功 1:已付款 2:已发货',
  `CREATE_TIME` datetime    NOT NULL COMMENT '创建时间',
  KEY `idx_create_time` (`CREATE_TIME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='秒杀成功明细表';


CREATE TABLE `meite_seckill`
(
  `SECKILLID`   bigint(20)                      NOT NULL COMMENT '商品库存id',
  `NAME`        varchar(120) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `INVENTORY`   int(11)                         NOT NULL COMMENT '库存数量',
  `START_TIME`  datetime                        NOT NULL COMMENT '秒杀开启时间',
  `END_TIME`    datetime                        NOT NULL COMMENT '秒杀结束时间',
  `CREATE_TIME` datetime                        NOT NULL COMMENT '创建时间',
  `VERSION`     bigint(20)                      NOT NULL DEFAULT '0',
  PRIMARY KEY (`SECKILLID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='秒杀库存表';