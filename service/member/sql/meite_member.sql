/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 127.0.0.1:3306
 Source Schema         : meite_member

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 03/03/2021 17:13:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for meite_user
-- ----------------------------
DROP TABLE IF EXISTS `meite_user`;
CREATE TABLE `meite_user`
(
    `USER_ID`     bigint(20)                                             NOT NULL AUTO_INCREMENT,
    `MOBILE`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `EMAIL`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `PASSWORD`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `USER_NAME`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `SEX`         tinyint(1)                                             NULL DEFAULT NULL,
    `AGE`         smallint(3)                                            NULL DEFAULT NULL,
    `CREATE_TIME` datetime(0)                                            NULL DEFAULT NULL,
    `UPDATE_TIME` datetime(0)                                            NULL DEFAULT NULL,
    `IS_AVALIBLE` tinyint(1)                                             NULL DEFAULT NULL,
    `PIC_IMG`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `QQ_OPENID`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `WX_OPENID`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `WB_OPENID`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meite_user
-- ----------------------------
INSERT INTO `meite_user`
VALUES (2, '15671564665', NULL, '96E79218965EB72C92A549DD5A330112', NULL, NULL, NULL,
        '2021-03-03 16:51:38', NULL, 1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for meite_user_token
-- ----------------------------
DROP TABLE IF EXISTS `meite_user_token`;
CREATE TABLE `meite_user_token`
(
    `id`              bigint(20)                                             NOT NULL AUTO_INCREMENT,
    `token`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `login_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `device_infor`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `is_availability` tinyint(1)                                             NULL DEFAULT NULL,
    `user_id`         bigint(20)                                             NOT NULL,
    `create_time`     datetime(0)                                            NULL DEFAULT NULL,
    `update_time`     datetime(0)                                            NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meite_user_token
-- ----------------------------
INSERT INTO `meite_user_token`
VALUES (2, 'mt.mb.loginPCd7a8135c0d684d5d9615ed2bb99d0133', 'PC', 'Chrome 8/87.0.4280.66', 0, 2,
        '2021-03-03 16:52:29', NULL);

SET FOREIGN_KEY_CHECKS = 1;
