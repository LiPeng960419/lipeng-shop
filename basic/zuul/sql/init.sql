SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `gateway_handler`;
CREATE TABLE `gateway_handler`
(
  `ID`            int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `HANDLERID`     varchar(64) DEFAULT NULL,
  `HANDLERNAME`   varchar(64) DEFAULT NULL,
  `PREHANDLERID`  varchar(64) DEFAULT NULL,
  `NEXTHANDLERID` varchar(64) DEFAULT NULL,
  `ISOPEN`        int(11)     DEFAULT NULL,
  `REVISION`      int(11)     DEFAULT NULL,
  `CREATED_BY`    varchar(32) DEFAULT NULL,
  `CREATED_TIME`  datetime    DEFAULT NULL,
  `UPDATED_BY`    varchar(32) DEFAULT NULL,
  `UPDATED_TIME`  datetime    DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gateway_handler
-- ----------------------------
INSERT INTO `gateway_handler`
VALUES ('1', 'blacklistHandler', 'blacklistHandler', null, 'apiAuthorityHandler', '1', null, null,
        '2019-11-21 09:14:56', null, '2019-11-21 09:15:00');
INSERT INTO `gateway_handler`
VALUES ('2', 'apiAuthorityHandler', 'apiAuthorityHandler', 'blacklistHandler', 'toVerifyMapHandler',
        '1', null, null, '2019-11-21 09:15:26', null, '2019-11-21 09:15:28');
INSERT INTO `gateway_handler`
VALUES ('3', 'toVerifyMapHandler', 'toVerifyMapHandler', 'apiAuthorityHandler',
        'currentLimitHandler', '1', null, null, '2019-11-21 09:16:06', null, '2019-11-21 09:16:08');
INSERT INTO `gateway_handler`
VALUES ('4', 'currentLimitHandler', 'currentLimitHandler', 'toVerifyMapHandler', null, '1', null,
        null, '2019-11-21 09:16:29', null, '2019-11-21 09:16:31');

DROP TABLE IF EXISTS `meite_app_info`;
CREATE TABLE `meite_app_info`
(
  `ID`           int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APPNAME`      varchar(64) DEFAULT NULL,
  `APPID`        varchar(64) DEFAULT NULL,
  `APPSECRET`    varchar(64) DEFAULT NULL,
  `AVAILABILITY` int(11)     DEFAULT NULL,
  `REVISION`     int(11)     DEFAULT NULL,
  `CREATED_BY`   varchar(32) DEFAULT NULL,
  `CREATED_TIME` datetime    DEFAULT NULL,
  `UPDATED_BY`   varchar(32) DEFAULT NULL,
  `UPDATED_TIME` datetime    DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `meite_blacklist`;
CREATE TABLE `meite_blacklist`
(
  `ID`               int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `IPADDRESS`        varchar(64) DEFAULT NULL,
  `RESTRICTION_TYPE` int(11)     DEFAULT NULL,
  `AVAILABILITY`     int(11)     DEFAULT NULL,
  `REVISION`         int(11)     DEFAULT NULL,
  `CREATED_BY`       varchar(32) DEFAULT NULL,
  `CREATED_TIME`     datetime    DEFAULT NULL,
  `UPDATED_BY`       varchar(32) DEFAULT NULL,
  `UPDATED_TIME`     datetime    DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;