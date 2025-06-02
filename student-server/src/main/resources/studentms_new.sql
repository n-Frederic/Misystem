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

-- 设置字符集及关闭外键检查
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------
-- 1. 教师表 t
-- -------------------------------------
DROP TABLE IF EXISTS `t`;
CREATE TABLE `t` (
                     `tid` INT NOT NULL AUTO_INCREMENT,
                     `tname` VARCHAR(30) NOT NULL,
                     `password` VARCHAR(30) NOT NULL,
                     PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `t` VALUES
                    (4, '陈旭东', 'cxd'),
                    (6, 'admin', '123'),
                    (13, '刘铎', 'ld'),
                    (14, '王方石', 'wfs');

-- -------------------------------------
-- 2. 课程表 c
-- -------------------------------------
DROP TABLE IF EXISTS `c`;
CREATE TABLE `c` (
                     `cid` INT NOT NULL AUTO_INCREMENT,
                     `cname` VARCHAR(30) NOT NULL,
                     `ccredit` TINYINT DEFAULT NULL,
                     `day` INT NOT NULL,
                     `period` INT NOT NULL,
                     `capacity` INT DEFAULT 30,
                     `selected` INT DEFAULT 0,
                     `location` VARCHAR(50) DEFAULT NULL,
                     PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `c` (`cid`, `cname`, `ccredit`, `day`, `period`, `capacity`, `selected`, `location`) VALUES
                                                                                                     (7, '数据结构', 3, 1, 1, 40, 0, 'YF505'),
                                                                                                     (8, '数据库系统', 3, 1, 4, 50, 0, 'YF409'),
                                                                                                     (9, '计算机网络', 3, 1, 5, 30, 0, 'YF312'),
                                                                                                     (10, '计算机组成原理', 3, 1, 6, 60, 0, 'YF404'),
                                                                                                     (11, '软件工程导论', 1, 1, 7, 20, 0, NULL),
                                                                                                     (12, '离散数学', 3, 2, 1, 40, 0, 'YF610'),
                                                                                                     (13, '人工智能基础', 2, 2, 2, 50, 0, 'SD107'),
                                                                                                     (14, '程序设计基础', 3, 2, 4, 30, 0, 'DQ408'),
                                                                                                     (15, '大数据概论', 2, 2, 6, 30, 0, 'YF501');

-- -------------------------------------
-- 3. 学生表 s
-- -------------------------------------
DROP TABLE IF EXISTS `s`;
CREATE TABLE `s` (
                     `sid` INT NOT NULL AUTO_INCREMENT,
                     `sname` VARCHAR(30) NOT NULL,
                     `password` VARCHAR(30) NOT NULL,
                     PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `s` VALUES
                    (1, '周学超', '123'),
                    (2, '姜雨彤', '123456'),
                    (3, '韦雪', 'pass01'),
                    (4, '黄爱雷', '111111'),
                    (5, '贾佩伦', 'root01'),
                    (6, '薛探昕', 'java22'),
                    (7, '胡佳乐', 'cstart'),
                    (8, '郭开蒙', 'sqlpwd'),
                    (9, '周十一', 'math01'),
                    (10, '郑十二', 'vue123');

-- -------------------------------------
-- 4. 课程-教师-学期表 ct
-- -------------------------------------
DROP TABLE IF EXISTS `ct`;
CREATE TABLE `ct` (
                      `ctid` INT NOT NULL AUTO_INCREMENT,
                      `cid` INT,
                      `tid` INT,
                      `term` VARCHAR(30) NOT NULL,
                      PRIMARY KEY (`ctid`),
                      FOREIGN KEY (`cid`) REFERENCES `c`(`cid`),
                      FOREIGN KEY (`tid`) REFERENCES `t`(`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `ct` VALUES
                     (1, 8, 4, '25-春季学期'),
                     (2, 7, 4, '25-春季学期'),
                     (3, 10, 13, '25-春季学期'),
                     (4, 9, 13, '25-春季学期'),
                     (5, 11, 4, '25-春季学期'),
                     (6, 9, 4, '25-秋季学期');

-- -------------------------------------
-- 5. 学生选课表 sct
-- -------------------------------------
DROP TABLE IF EXISTS `sct`;
CREATE TABLE `sct` (
                       `sctid` INT NOT NULL AUTO_INCREMENT,
                       `sid` INT,
                       `cid` INT,
                       `tid` INT,
                       `grade` FLOAT DEFAULT NULL,
                       `term` VARCHAR(30) DEFAULT NULL,
                       PRIMARY KEY (`sctid`),
                       FOREIGN KEY (`sid`) REFERENCES `s`(`sid`),
                       FOREIGN KEY (`cid`) REFERENCES `c`(`cid`),
                       FOREIGN KEY (`tid`) REFERENCES `t`(`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入前先建触发器
DROP TRIGGER IF EXISTS `after_sct_insert`;
DELIMITER //
CREATE TRIGGER `after_sct_insert`
    AFTER INSERT ON `sct`
    FOR EACH ROW
BEGIN
    UPDATE `c` SET `selected` = `selected` + 1 WHERE `cid` = NEW.`cid`;
END;
//
DELIMITER ;

-- 插入数据（插入后会自动更新课程的 selected 字段）
INSERT INTO `sct` VALUES
                      (10, 2, 8, 4, 2, '25-春季学期'),
                      (11, 2, 10, 13, NULL, '25-春季学期'),
                      (12, 2, 7, 4, 5, '25-春季学期'),
                      (13, 4, 8, 4, 10, '25-春季学期'),
                      (14, 4, 7, 4, NULL, '25-春季学期'),
                      (15, 4, 10, 13, NULL, '25-春季学期'),
                      (16, 1, 8, 4, NULL, '25-春季学期'),
                      (17, 1, 10, 13, NULL, '25-春季学期');

-- 开启外键检查
SET FOREIGN_KEY_CHECKS = 1;

