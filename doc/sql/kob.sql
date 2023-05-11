/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : kob

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 11/05/2023 09:38:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bot
-- ----------------------------
DROP TABLE IF EXISTS `bot`;
CREATE TABLE `bot` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，唯一',
  `user_id` int DEFAULT NULL COMMENT 'bot所属唯一的id',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `content` varchar(10000) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of bot
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `a_id` int DEFAULT NULL COMMENT 'a的id',
  `a_sx` int DEFAULT NULL COMMENT 'a开始坐标的x',
  `a_sy` int DEFAULT NULL COMMENT 'a开始坐标的y',
  `b_id` int DEFAULT NULL COMMENT 'b的id',
  `b_sx` int DEFAULT NULL COMMENT 'b开始坐标的x',
  `b_sy` int DEFAULT NULL COMMENT 'b开始坐标的y',
  `a_steps` varchar(1000) DEFAULT NULL COMMENT '所有方向的集合',
  `b_steps` varchar(1000) DEFAULT NULL COMMENT 'b所有方向的集合',
  `map` varchar(1000) DEFAULT NULL COMMENT '地图',
  `loser` varchar(10) DEFAULT NULL COMMENT '输家',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `avatar` varchar(1000) DEFAULT NULL COMMENT '头像',
  `rating` int DEFAULT '1500',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
