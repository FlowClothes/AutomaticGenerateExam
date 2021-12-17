/*
 Navicat Premium Data Transfer

 Source Server         : one
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : hanlp

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 17/12/2021 08:47:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `article_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '文章编号',
  `articleName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章/文件名',
  `uploader` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传者',
  `uploadTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传时间',
  `articleType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `articleSize` double NOT NULL COMMENT '大小',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `articleState` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章上传状态',
  PRIMARY KEY (`article_id`) USING BTREE,
  UNIQUE INDEX `articleName`(`articleName`) USING BTREE COMMENT '文章名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for noun
-- ----------------------------
DROP TABLE IF EXISTS `noun`;
CREATE TABLE `noun`  (
  `nounid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实体名词编号',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实体内容',
  `attribute` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性',
  PRIMARY KEY (`nounid`) USING BTREE,
  UNIQUE INDEX `nouncontent`(`content`) USING BTREE COMMENT '实体名词内容唯一'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for predicate
-- ----------------------------
DROP TABLE IF EXISTS `predicate`;
CREATE TABLE `predicate`  (
  `predicateid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '谓语编号',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '谓语内容',
  PRIMARY KEY (`predicateid`) USING BTREE,
  UNIQUE INDEX `content`(`content`) USING BTREE COMMENT '谓语内容唯一'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation`  (
  `relationid` int(0) NOT NULL AUTO_INCREMENT COMMENT '关系编号',
  `subjectid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主语',
  `predicateid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '谓语',
  `objectid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '宾语',
  `objectid2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宾语2',
  `objectid3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宾语3',
  `objectid4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宾语4',
  `objectid5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宾语5',
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `adverbial` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状语备用',
  PRIMARY KEY (`relationid`) USING BTREE,
  INDEX `subjectid`(`subjectid`) USING BTREE,
  INDEX `predicateid`(`predicateid`) USING BTREE,
  INDEX `objectid`(`objectid`) USING BTREE,
  INDEX `objectid2`(`objectid2`) USING BTREE,
  INDEX `objectid3`(`objectid3`) USING BTREE,
  INDEX `objectid4`(`objectid4`) USING BTREE,
  INDEX `objectid5`(`objectid5`) USING BTREE,
  CONSTRAINT `relation_ibfk_1` FOREIGN KEY (`subjectid`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_2` FOREIGN KEY (`predicateid`) REFERENCES `predicate` (`predicateid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_3` FOREIGN KEY (`objectid`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_4` FOREIGN KEY (`objectid2`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_5` FOREIGN KEY (`objectid3`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_6` FOREIGN KEY (`objectid4`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ibfk_7` FOREIGN KEY (`objectid5`) REFERENCES `noun` (`nounid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 304 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `uname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `permission` int(0) NOT NULL COMMENT '用户权限',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username`(`uname`) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 2);

SET FOREIGN_KEY_CHECKS = 1;
