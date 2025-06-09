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

-- 删除并重建 ct 表，添加 cno 作为主键
DROP TABLE IF EXISTS `ct`;
CREATE TABLE `ct` (
                      `cno` INT NOT NULL,                    -- 开课编号，作为主键
                      `cid` INT,                             -- 课程ID
                      `tid` INT,                             -- 教师ID
                      `term` VARCHAR(30) NOT NULL,           -- 学期
                      PRIMARY KEY (`cno`),
                      FOREIGN KEY (`cid`) REFERENCES `c`(`cid`),
                      FOREIGN KEY (`tid`) REFERENCES `t`(`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入数据到 ct 表（使用 cno 作为主键）
INSERT INTO `ct` (`cno`, `cid`, `tid`, `term`) VALUES
                                                   (2001, 8, 4, '25-春季学期'),    -- 数据库系统-陈旭东-25春季
                                                   (2002, 7, 4, '25-春季学期'),    -- 数据结构-陈旭东-25春季
                                                   (2003, 10, 13, '25-春季学期'),  -- 计算机组成原理-刘铎-25春季
                                                   (2004, 9, 13, '25-春季学期'),   -- 计算机网络-刘铎-25春季
                                                   (2005, 11, 4, '25-春季学期'),   -- 软件工程导论-陈旭东-25春季
                                                   (2006, 9, 4, '25-秋季学期');    -- 计算机网络-陈旭东-25秋季

-- 创建新的 sc 表（学生选课表）
DROP TABLE IF EXISTS `sc`;
CREATE TABLE `sc` (
                      `sid` INT NOT NULL,                    -- 学生ID
                      `cno` INT NOT NULL,                    -- 开课编号
                      `daily_grade` FLOAT DEFAULT NULL,      -- 平时成绩
                      `term` VARCHAR(20),
                      PRIMARY KEY (`sid`, `cno`),            -- 联合主键
                      FOREIGN KEY (`sid`) REFERENCES `s`(`sid`),
                      FOREIGN KEY (`cno`) REFERENCES `ct`(`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建触发器，在插入选课记录时自动更新课程的已选人数
DROP TRIGGER IF EXISTS `after_sc_insert`;
DELIMITER //
CREATE TRIGGER `after_sc_insert`
    AFTER INSERT ON `sc`
    FOR EACH ROW
BEGIN
    UPDATE `c` SET `selected` = `selected` + 1
    WHERE `cid` = (SELECT `cid` FROM `ct` WHERE `cno` = NEW.`cno`);
END;
//
DELIMITER ;

-- 创建删除触发器，删除选课记录时自动减少课程的已选人数
DROP TRIGGER IF EXISTS `after_sc_delete`;
DELIMITER //
CREATE TRIGGER `after_sc_delete`
    AFTER DELETE ON `sc`
    FOR EACH ROW
BEGIN
    UPDATE `c` SET `selected` = `selected` - 1
    WHERE `cid` = (SELECT `cid` FROM `ct` WHERE `cno` = OLD.`cno`);
END;
//
DELIMITER ;

-- 插入数据到 sc 表
INSERT INTO `sc` (`sid`, `cno`, `daily_grade`,`term`) VALUES
                                                                  (2, 2001, NULL,'25-春季学期'),       -- 姜雨彤选择数据库系统(陈旭东-25春季)，期末成绩2
                                                                  (2, 2003, NULL,'25-春季学期'),    -- 姜雨彤选择计算机组成原理(刘铎-25春季)，暂无成绩
                                                                  (2, 2002, NULL,'25-春季学期'),       -- 姜雨彤选择数据结构(陈旭东-25春季)，期末成绩5
                                                                  (4, 2001, NULL,'25-春季学期'),      -- 黄爱雷选择数据库系统(陈旭东-25春季)，期末成绩10
                                                                  (4, 2002, NULL,'25-春季学期'),    -- 黄爱雷选择数据结构(陈旭东-25春季)，暂无成绩
                                                                  (4, 2003, NULL,'25-春季学期'),    -- 黄爱雷选择计算机组成原理(刘铎-25春季)，暂无成绩
                                                                  (1, 2001, NULL,'25-春季学期'),    -- 周学超选择数据库系统(陈旭东-25春季)，暂无成绩
                                                                  (1, 2003, NULL,'25-春季学期');    -- 周学超选择计算机组成原理(刘铎-25春季)，暂无成绩

-- 删除并重建 Exam 表，主键为 cno，参照 ct 表
DROP TABLE IF EXISTS `Exam`;
CREATE TABLE `Exam` (
                        `cno` INT NOT NULL,                    -- 开课编号，作为主键
                        `day`  DATE,                             -- 考试日期
                        `period` INT,                          -- 考试时段
                        `durationMin` INT,                     -- 考试时长（分钟）
                        `grade` FLOAT,                           -- 考试总分
                        `location` VARCHAR(50),                -- 考试地点
                        `Req` VARCHAR(255),                    -- 考试要求
                        PRIMARY KEY (`cno`),
                        FOREIGN KEY (`cno`) REFERENCES `ct`(`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `Exam` (`cno`, `day`, `period`, `durationMin`, `grade`, `location`, `Req`) VALUES
                                                                                           (2001, '2025-06-15', 4, 120, 100, 'YF409', '遵守考场纪律，保持安静'),                    -- 数据库系统考试
                                                                                           (2002, '2025-06-15', 1, 120, 100, 'YF505', '携带学生证和文具，禁止作弊'),                -- 数据结构考试
                                                                                           (2003, '2025-06-15', 6, 120, 100, 'YF404', '独立完成考试，不得交头接耳'),                -- 计算机组成原理考试
                                                                                           (2004, '2025-06-15', 5, 120, 100, 'YF312', '提前 15 分钟入场，关闭手机'),               -- 计算机网络考试
                                                                                           (2005, '2025-06-15', 7, 120, 100, NULL, '认真审题，规范答题'),                          -- 软件工程导论考试
                                                                                           (2006, '2025-12-15', 5, 120, 100, 'YF312', '提前 15 分钟入场，关闭手机');               -- 计算机网络考试(秋季)                                                                                          (2006, 3, 5, 120, 100, 'YF312', '提前 15 分钟入场，关闭手机');               -- 计算机网络考试(秋季)                                                                                      (2006, 3, 5, 120, 100, 'YF312', '提前 15 分钟入场，关闭手机');               -- 计算机网络考试(秋季)
SET FOREIGN_KEY_CHECKS = 1;

