/*
 Navicat Premium Data Transfer

 Source Server         : myBase
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : studentms

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 01/06/2025 17:09:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for c
-- ----------------------------
DROP TABLE IF EXISTS `c`;
CREATE TABLE `c`  (
  `cid` int NOT NULL AUTO_INCREMENT,
  `cname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ccredit` tinyint NULL DEFAULT NULL,
  `day` int not null,
  `period` int not null,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c
-- ----------------------------
INSERT INTO `c` VALUES (7, '数据结构', 3, 1, 1);
INSERT INTO `c` VALUES (8, '数据库系统', 3, 1, 4);
INSERT INTO `c` VALUES (9, '计算机网络', 3, 1, 5);
INSERT INTO `c` VALUES (10, '计算机组成原理', 3, 1, 6);
INSERT INTO `c` VALUES (11, '软件工程导论', 1, 1, 7);
INSERT INTO `c` VALUES (12, '离散数学', 3, 2, 1);
INSERT INTO `c` VALUES (13, '人工智能基础', 2, 2, 2);
INSERT INTO `c` VALUES (14, '程序设计基础', 3, 2, 4);
INSERT INTO `c` VALUES (15, '大数据概论', 2, 2, 6);

-- ----------------------------
-- Table structure for ct
-- ----------------------------
DROP TABLE IF EXISTS `ct`;
CREATE TABLE `ct`  (
  `ctid` int NOT NULL AUTO_INCREMENT,
  `cid` int NULL DEFAULT NULL,
  `tid` int NULL DEFAULT NULL,
  `term` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ctid`) USING BTREE,
  INDEX `cid`(`cid` ASC) USING BTREE,
  INDEX `tid`(`tid` ASC) USING BTREE,
  CONSTRAINT `ct_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `c` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ct_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `t` (`tid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ct
-- ----------------------------
INSERT INTO `ct` VALUES (1, 8, 4, '25-春季学期');
INSERT INTO `ct` VALUES (2, 7, 4, '25-春季学期');
INSERT INTO `ct` VALUES (3, 10, 13, '25-春季学期');
INSERT INTO `ct` VALUES (4, 9, 13, '25-春季学期');
INSERT INTO `ct` VALUES (5, 11, 4, '25-春季学期');
INSERT INTO `ct` VALUES (6, 9, 4, '25-秋季学期');

-- ----------------------------
-- Table structure for s
-- ----------------------------
DROP TABLE IF EXISTS `s`;
CREATE TABLE `s`  (
  `sid` int NOT NULL AUTO_INCREMENT,
  `sname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of s
-- ----------------------------
INSERT INTO `s` VALUES (1, '周学超', '123');
INSERT INTO `s` VALUES (2, '姜雨彤', '123456');
INSERT INTO `s` VALUES (3, '韦雪', 'pass01');
INSERT INTO `s` VALUES (4, '黄爱雷', '111111');
INSERT INTO `s` VALUES (5, '贾佩伦', 'root01');
INSERT INTO `s` VALUES (6, '薛探昕', 'java22');
INSERT INTO `s` VALUES (7, '胡佳乐', 'cstart');
INSERT INTO `s` VALUES (8, '郭开蒙', 'sqlpwd');
INSERT INTO `s` VALUES (9, '周十一', 'math01');
INSERT INTO `s` VALUES (10, '郑十二', 'vue123');

-- ----------------------------
-- Table structure for sct
-- ----------------------------
DROP TABLE IF EXISTS `sct`;
CREATE TABLE `sct`  (
  `sctid` int NOT NULL AUTO_INCREMENT,
  `sid` int NULL DEFAULT NULL,
  `cid` int NULL DEFAULT NULL,
  `tid` int NULL DEFAULT NULL,
  `grade` float NULL DEFAULT NULL,
  `term` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sctid`) USING BTREE,
  INDEX `sid`(`sid` ASC) USING BTREE,
  INDEX `tid`(`tid` ASC) USING BTREE,
  INDEX `cid`(`cid` ASC) USING BTREE,
  CONSTRAINT `sct_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `s` (`sid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sct_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `ct` (`tid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sct_ibfk_3` FOREIGN KEY (`cid`) REFERENCES `ct` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sct
-- ----------------------------
INSERT INTO `sct` VALUES (10, 2, 8, 4, 2, '25-春季学期');
INSERT INTO `sct` VALUES (11, 2, 10, 13, NULL, '25-春季学期');
INSERT INTO `sct` VALUES (12, 2, 7, 4, 5, '25-春季学期');
INSERT INTO `sct` VALUES (13, 4, 8, 4, 10, '25-春季学期');
INSERT INTO `sct` VALUES (14, 4, 7, 4, NULL, '25-春季学期');
INSERT INTO `sct` VALUES (15, 4, 10, 13, NULL, '25-春季学期');
INSERT INTO `sct` VALUES (16, 1, 8, 4, NULL, '25-春季学期');
INSERT INTO `sct` VALUES (17, 1, 10, 13, NULL, '25-春季学期');

-- ----------------------------
-- Table structure for t
-- ----------------------------
DROP TABLE IF EXISTS `t`;
CREATE TABLE `t`  (
  `tid` int NOT NULL AUTO_INCREMENT,
  `tname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t
-- ----------------------------
INSERT INTO `t` VALUES (4, '陈旭东', 'cxd');
INSERT INTO `t` VALUES (6, 'admin', '123');
INSERT INTO `t` VALUES (13, '刘铎', 'ld');
INSERT INTO `t` VALUES (14, '王方石', 'wfs');

SET FOREIGN_KEY_CHECKS = 1;
