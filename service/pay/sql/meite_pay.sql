SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `payment_channel`;
CREATE TABLE `payment_channel`
(
  `ID`            int(11)     NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CHANNEL_NAME`  varchar(32) NOT NULL COMMENT '渠道名称',
  `CHANNEL_ID`    varchar(32) NOT NULL COMMENT '渠道ID',
  `MERCHANT_ID`   varchar(32) NOT NULL COMMENT '商户id',
  `SYNC_URL`      text        NOT NULL COMMENT '同步回调URL',
  `ASYN_URL`      text        NOT NULL COMMENT '异步回调URL',
  `PUBLIC_KEY`    text COMMENT '公钥',
  `PRIVATE_KEY`   text COMMENT '私钥',
  `CHANNEL_STATE` int(11)     DEFAULT '0' COMMENT '渠道状态 0开启1关闭',
  `CLASS_ADDRESS` varchar(64) DEFAULT '' COMMENT '类路径地址',
  `REVISION`      int(11)     DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY`    varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME`  datetime    DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY`    varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME`  datetime    DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`, `CHANNEL_ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8 COMMENT ='支付渠道';

INSERT INTO `payment_channel`
VALUES ('1', '银联支付', 'yinlian_pay', '777290058110048', 'http://www.coship.com',
        'http://b7e244bb.ngrok.io/unionPayAsynCallback', null, null, '0',
        'com.lipeng.pay.strategy.impl.UnionPayStrategy', null, null, null, null, null);
INSERT INTO `payment_channel`
VALUES ('2', '支付宝支付', 'alipay', '0', 'http://14320ed2.ngrok.io/alipay/callBack/synCallBack',
        'http://b7e244bb.ngrok.io/aliPayAsynCallback', null, null, '0',
        'com.lipeng.pay.strategy.impl.AliPayStrategy', null, null, null, null, null);
INSERT INTO `payment_channel`
VALUES ('3', '支付宝手机支付', 'ali_mobile_pay', '0', 'http://14320ed2.ngrok.io/alipay/callBack/synMobileCallBack',
        'http://b7e244bb.ngrok.io/aliMobilePayAsynCallback', null, null, '0',
        'com.lipeng.pay.strategy.impl.AliMobilePayStrategy', null, null, null, null, null);
INSERT INTO `payment_channel`
VALUES ('4', '支付宝扫码支付', 'ali_f2f_pay', '0', 'http://14320ed2.ngrok.io/alipay/callBack/synF2FCallBack',
        'http://b7e244bb.ngrok.io/aliPayF2FAsynCallback', null, null, '0',
        'com.lipeng.pay.strategy.impl.AliF2FPayStrategy', null, null, null, null, null);
INSERT INTO `payment_channel`
VALUES ('5', '支付宝app支付', 'ali_app_pay', '0', 'http://14320ed2.ngrok.io/alipay/callBack/synAppCallBack',
        'http://b7e244bb.ngrok.io/aliPayAppAsynCallback', null, null, '0',
        'com.lipeng.pay.strategy.impl.AliAppPayStrategy', null, null, null, null, null);

DROP TABLE IF EXISTS `payment_transaction`;
CREATE TABLE `payment_transaction`
(
  `ID`              int(11)     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PAY_AMOUNT`      int(11)     NOT NULL COMMENT '支付金额',
  `PAYMENT_STATUS`  int(11)     NOT NULL DEFAULT '0' COMMENT '支付状态 0待支付1已经支付2支付超时3支付失败',
  `USER_ID`         int(11)     NOT NULL COMMENT '用户ID',
  `ORDER_ID`        varchar(32) NOT NULL COMMENT '订单号码',
  `REVISION`        int(11)              DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY`      varchar(32)          DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME`    datetime             DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY`      varchar(32)          DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME`    datetime             DEFAULT NULL COMMENT '更新时间',
  `PARTY_PAYID`     varchar(64)          DEFAULT NULL COMMENT '第三方支付交易ID',
  `PAYMENT_ID`      varchar(64)          DEFAULT NULL COMMENT '支付ID唯一',
  `PAYMENT_CHANNEL` varchar(32)          DEFAULT NULL COMMENT '支付渠道',
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='支付交易';

DROP TABLE IF EXISTS `payment_transaction_log`;
CREATE TABLE `payment_transaction_log`
(
  `ID`             int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `SYNCH_LOG`      text COMMENT '同步回调日志',
  `ASYNC_LOG`      text COMMENT '异步回调日志',
  `CHANNEL_ID`     varchar(32) DEFAULT NULL COMMENT '支付渠道ID',
  `TRANSACTION_ID` varchar(64) DEFAULT NULL COMMENT '支付交易ID',
  `REVISION`       int(11)     DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY`     varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME`   datetime    DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY`     varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME`   datetime    DEFAULT NULL COMMENT '更新时间',
  `UNTITLED`       varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='支付交易日志表';

-- 表 broker_message_log 消息记录结构
CREATE TABLE IF NOT EXISTS `broker_message_log`
(
  `message_id`  varchar(128) NOT NULL, -- 消息唯一ID
  `message`     varchar(4000)         DEFAULT NULL, -- 消息内容
  `try_count`   int(4)                DEFAULT '0', -- 重试次数
  `status`      varchar(10)           DEFAULT '', -- 消息投递状态  0 投递中 1 投递成功   2 投递失败
  `next_retry`  timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00', -- 下一次重试时间 或 超时时间
  `create_time` timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00', -- 创建时间
  `update_time` timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00', -- 更新时间
  PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;