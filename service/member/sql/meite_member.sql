/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50715
Source Host           : 127.0.0.1:3306
Source Database       : meite_member

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2019-11-14 21:14:55
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for `meite_user`
-- ----------------------------
DROP TABLE IF EXISTS `meite_user`;
CREATE TABLE `meite_user`
(
  `USER_ID`     bigint(20) NOT NULL AUTO_INCREMENT,
  `MOBILE`      varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `EMAIL`       varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `PASSWORD`    varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `USER_NAME`   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `SEX`         tinyint(1)                       DEFAULT NULL,
  `AGE`         smallint(3)                      DEFAULT NULL,
  `CREATE_TIME` datetime                         DEFAULT NULL,
  `UPDATE_TIME` datetime                         DEFAULT NULL,
  `IS_AVALIBLE` tinyint(1)                       DEFAULT NULL,
  `PIC_IMG`     varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `QQ_OPENID`   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `WX_OPENID`   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `WB_OPENID`   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- ----------------------------
-- Records of meite_user
-- ----------------------------
INSERT INTO `meite_user`
VALUES ('1', '15671564665', null, 'E10ADC3949BA59ABBE56E057F20F883E', 'Insomnia', '0', null,
        '2019-11-11 09:36:06', '2019-11-12 09:41:16', '1', null, null,
        'o1eIhxH9G8AdOSEQuyRai1LkEceo');

-- ----------------------------
-- Table structure for `meite_user_token`
-- ----------------------------
DROP TABLE IF EXISTS `meite_user_token`;
CREATE TABLE `meite_user_token`
(
  `id`              bigint(20) NOT NULL AUTO_INCREMENT,
  `token`           varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `login_type`      varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `device_infor`    varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_availability` tinyint(1)                       DEFAULT NULL,
  `user_id`         bigint(20) NOT NULL,
  `create_time`     datetime                         DEFAULT NULL,
  `update_time`     datetime                         DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- ----------------------------
-- Records of meite_user_token
-- ----------------------------
INSERT INTO `meite_user_token`
VALUES ('1', 'mt.mb.loginPCe69d7f308c27489dbc6314da54dbe388', 'PC', 'Chrome/75.0.3770.100', '0',
        '1', '2019-11-12 09:19:49', '2019-11-12 21:00:44');