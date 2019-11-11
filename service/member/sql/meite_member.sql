/*
Navicat MySQL Data Transfer

Source Server         : 192.168.199.254
Source Server Version : 50722
Source Host           : 192.168.199.254:3306
Source Database       : meite_member

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-11-11 23:26:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for meite_user
-- ----------------------------
DROP TABLE IF EXISTS `meite_user`;
CREATE TABLE `meite_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MOBILE` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `EMAIL` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `PASSWORD` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `USER_NAME` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `SEX` tinyint(1) DEFAULT NULL,
  `AGE` smallint(3) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `IS_AVALIBLE` tinyint(1) DEFAULT NULL,
  `PIC_IMG` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `QQ_OPENID` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `WX_OPENID` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of meite_user
-- ----------------------------
INSERT INTO `meite_user` VALUES ('1', '15671564665', null, 'E10ADC3949BA59ABBE56E057F20F883E', '李鹏', '0', '24', '2019-11-11 23:10:20', null, '1', null, null, null);

-- ----------------------------
-- Table structure for meite_user_token
-- ----------------------------
DROP TABLE IF EXISTS `meite_user_token`;
CREATE TABLE `meite_user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `login_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `device_infor` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_availability` tinyint(1) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of meite_user_token
-- ----------------------------
