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
  DEFAULT CHARSET = utf8;

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