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
/* ---------- 公共设置 ---------- */
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

/* ---------- 1. 教师表 t ---------- */
DROP TABLE IF EXISTS `t`;
CREATE TABLE `t` (
                     `tid` INT NOT NULL AUTO_INCREMENT,
                     `tname` VARCHAR(30) NOT NULL,
                     `password` VARCHAR(30) NOT NULL,
                     PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `t` VALUES
                    (4,'陈旭东','cxd'),
                    (6,'admin','123'),
                    (13,'刘铎','ld'),
                    (14,'王方石','wfs');

/* ---------- 2. 课程表 c（已剔除上课安排字段） ---------- */
DROP TABLE IF EXISTS `c`;
CREATE TABLE `c` (
                     `cid` INT NOT NULL AUTO_INCREMENT,
                     `cname` VARCHAR(30) NOT NULL,
                     `ccredit` TINYINT DEFAULT NULL,
                     PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `c` (`cid`,`cname`,`ccredit`) VALUES
                                              (7 ,'数据结构',3),
                                              (8 ,'数据库系统',3),
                                              (9 ,'计算机网络',3),
                                              (10,'计算机组成原理',3),
                                              (11,'软件工程导论',1),
                                              (12,'离散数学',3),
                                              (13,'人工智能基础',2),
                                              (14,'程序设计基础',3),
                                              (15,'大数据概论',2);

/* ---------- 3. 学生表 s ---------- */
DROP TABLE IF EXISTS `s`;
CREATE TABLE `s` (
                     `sid` INT NOT NULL AUTO_INCREMENT,
                     `sname` VARCHAR(30) NOT NULL,
                     `password` VARCHAR(30) NOT NULL,
                     PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `s` VALUES
                    (1,'周学超','123'),
                    (2,'姜雨彤','123456'),
                    (3,'韦雪','pass01'),
                    (4,'黄爱雷','111111'),
                    (5,'贾佩伦','root01'),
                    (6,'薛探昕','java22'),
                    (7,'胡佳乐','cstart'),
                    (8,'郭开蒙','sqlpwd'),
                    (9,'周十一','math01'),
                    (10,'郑十二','vue123');

/* ---------- 4. 开课表 ct（新增 5 个列） ---------- */
DROP TABLE IF EXISTS `ct`;
CREATE TABLE `ct` (
                      `cno` INT NOT NULL,         -- 开课编号
                      `cid` INT,                  -- 课程ID
                      `tid` INT,                  -- 教师ID
                      `term` VARCHAR(30) NOT NULL,-- 学期
                      `day` INT NOT NULL,         -- 上课星期（1-7）
                      `period` INT NOT NULL,      -- 上课节次
                      `capacity` INT DEFAULT 30,  -- 课容量
                      `selected` INT DEFAULT 0,   -- 已选人数
                      `location` VARCHAR(50),     -- 上课地点
                      PRIMARY KEY (`cno`),
                      FOREIGN KEY (`cid`) REFERENCES `c`(`cid`),
                      FOREIGN KEY (`tid`) REFERENCES `t`(`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* —— 同步原有课程安排到 ct —— */
INSERT INTO `ct`
(`cno`,`cid`,`tid`,`term`,`day`,`period`,`capacity`,`selected`,`location`) VALUES
                                                                               (2001,8 ,4 ,'25-春季学期',1,4,50,0,'YF409'),  -- 数据库系统
                                                                               (2002,7 ,4 ,'25-春季学期',1,1,40,0,'YF505'),  -- 数据结构
                                                                               (2003,10,13,'25-春季学期',1,6,60,0,'YF404'),  -- 组成原理
                                                                               (2004,9 ,13,'25-春季学期',1,5,30,0,'YF312'),  -- 计算机网络
                                                                               (2005,11,4 ,'25-春季学期',1,7,20,0,NULL),     -- 软件工程导论
                                                                               (2006,9 ,4 ,'25-秋季学期',1,5,30,0,'YF312');  -- 网络（秋季）

/* ---------- 5. 学生选课表 sc ---------- */
DROP TABLE IF EXISTS `sc`;
CREATE TABLE `sc` (
                      `sid` INT NOT NULL,
                      `cno` INT NOT NULL,
                      `daily_grade` FLOAT DEFAULT NULL,
                      `term` VARCHAR(20),
                      PRIMARY KEY (`sid`,`cno`),
                      FOREIGN KEY (`sid`) REFERENCES `s`(`sid`),
                      FOREIGN KEY (`cno`) REFERENCES `ct`(`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `sc` (`sid`,`cno`,`daily_grade`,`term`) VALUES
                                                        (2,2001,NULL,'25-春季学期'),
                                                        (2,2003,NULL,'25-春季学期'),
                                                        (2,2002,NULL,'25-春季学期'),
                                                        (4,2001,NULL,'25-春季学期'),
                                                        (4,2002,NULL,'25-春季学期'),
                                                        (4,2003,NULL,'25-春季学期'),
                                                        (1,2001,NULL,'25-春季学期'),
                                                        (1,2003,NULL,'25-春季学期');

/* ---------- 6. 成绩触发器：改为维护 ct.selected ---------- */
DROP TRIGGER IF EXISTS `after_sc_insert`;
DELIMITER //
CREATE TRIGGER `after_sc_insert`
    AFTER INSERT ON `sc`
    FOR EACH ROW
BEGIN
    UPDATE `ct` SET `selected` = `selected` + 1
    WHERE `cno` = NEW.`cno`;
END;
//
DELIMITER ;

DROP TRIGGER IF EXISTS `after_sc_delete`;
DELIMITER //
CREATE TRIGGER `after_sc_delete`
    AFTER DELETE ON `sc`
    FOR EACH ROW
BEGIN
    UPDATE `ct` SET `selected` = `selected` - 1
    WHERE `cno` = OLD.`cno`;
END;
//
DELIMITER ;

/* ---------- 7. Exam 表（保持不变，仅展示） ---------- */
DROP TABLE IF EXISTS `Exam`;
CREATE TABLE `Exam` (
                        `cno` INT NOT NULL,
                        `day` DATE,
                        `period` INT,
                        `durationMin` INT,
                        `grade` FLOAT,
                        `location` VARCHAR(50),
                        `Req` VARCHAR(255),
                        PRIMARY KEY (`cno`),
                        FOREIGN KEY (`cno`) REFERENCES `ct`(`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `Exam`
(`cno`,`day`,`period`,`durationMin`,`grade`,`location`,`Req`) VALUES
                                                                  (2001,'2025-06-15',4,120,100,'YF409','遵守考场纪律，保持安静'),
                                                                  (2002,'2025-06-15',1,120,100,'YF505','携带学生证和文具，禁止作弊'),
                                                                  (2003,'2025-06-15',6,120,100,'YF404','独立完成考试，不得交头接耳'),
                                                                  (2004,'2025-06-15',5,120,100,'YF312','提前 15 分钟入场，关闭手机'),
                                                                  (2005,'2025-06-15',7,120,100,NULL   ,'认真审题，规范答题'),
                                                                  (2006,'2025-12-15',5,120,100,'YF312','提前 15 分钟入场，关闭手机');

SET FOREIGN_KEY_CHECKS = 1;
