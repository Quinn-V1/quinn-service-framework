/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50729
Source Host           : 212.64.54.213:3306
Source Database       : bpm

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-04-08 23:52:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `CO_SY_ID_GENERATOR`
-- ----------------------------
DROP TABLE IF EXISTS `CO_SY_ID_GENERATOR`;
CREATE TABLE `CO_SY_ID_GENERATOR` (
                                      `SEQ_NAME`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID类型' ,
                                      `ID_VALUE`  bigint(20) NULL DEFAULT 1 COMMENT 'ID值' ,
                                      `ID_STEP`  int(1) NULL DEFAULT 1 COMMENT '增长步长' ,
                                      PRIMARY KEY (`SEQ_NAME`)
)
    ENGINE=InnoDB
    DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_unicode_ci

;

-- ----------------------------
-- Records of CO_SY_ID_GENERATOR
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Function structure for `nextValue`
-- ----------------------------
DROP FUNCTION IF EXISTS `nextValue`;
DELIMITER ;;
CREATE FUNCTION `nextValue`(`seqName` varchar(200)) RETURNS bigint(20)
BEGIN
    DECLARE result BIGINT DEFAULT 1;
    SET result=(select max(ID_VALUE) from CO_SY_ID_GENERATOR WHERE SEQ_NAME = `seqName`);
    IF result IS NULL THEN
        INSERT INTO CO_SY_ID_GENERATOR(SEQ_NAME) VALUES(`seqName`);
        SET result=(select max(ID_VALUE) from CO_SY_ID_GENERATOR WHERE SEQ_NAME = `seqName`);
    END IF;

    UPDATE CO_SY_ID_GENERATOR SET ID_VALUE = ID_VALUE + ID_STEP WHERE SEQ_NAME = `seqName`;
    RETURN result;
END
;;
DELIMITER ;
