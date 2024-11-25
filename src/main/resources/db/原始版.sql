-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 150.158.150.143    Database: geology_db1
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrators`
--

DROP TABLE IF EXISTS `administrators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrators` (
  `a_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `a_password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `a_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `a_id` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `a_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `a_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `a_level` int NOT NULL COMMENT '管理员级别',
  PRIMARY KEY (`a_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrators`
--

LOCK TABLES `administrators` WRITE;
/*!40000 ALTER TABLE `administrators` DISABLE KEYS */;
/*!40000 ALTER TABLE `administrators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `engineering`
--

DROP TABLE IF EXISTS `engineering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `engineering` (
  `engineering_team_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工程队名称',
  `number` int NOT NULL COMMENT '人数',
  `s_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主管人姓名',
  `s_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  PRIMARY KEY (`engineering_team_name`),
  CONSTRAINT `engineering_check` CHECK ((`number` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `engineering`
--

LOCK TABLES `engineering` WRITE;
/*!40000 ALTER TABLE `engineering` DISABLE KEYS */;
/*!40000 ALTER TABLE `engineering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_info`
--

DROP TABLE IF EXISTS `image_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_info` (
  `image_id` int NOT NULL AUTO_INCREMENT COMMENT '图像编号',
  `image_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图像名称',
  `image_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图像存储路径',
  `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `uploader_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上传者',
  `image_sid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '岩芯ID',
  `ima_start` decimal(10,3) NOT NULL COMMENT '段开始',
  `ima_end` decimal(10,3) NOT NULL COMMENT '段深度',
  `s_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岩芯段类型',
  `ima_depth` decimal(10,3) NOT NULL COMMENT '段深度',
  PRIMARY KEY (`image_id`),
  UNIQUE KEY `image_info_unique` (`image_name`),
  KEY `image_info_Stratums_FK` (`image_sid`) USING BTREE,
  KEY `image_info_users_FK` (`uploader_num`) USING BTREE,
  KEY `index_image_one` (`image_name`,`ima_start`,`ima_end`,`ima_depth`,`image_path`,`s_type`) USING BTREE,
  KEY `index_image_two` (`image_name`,`image_path`,`upload_time`,`uploader_num`,`image_sid`) USING BTREE,
  CONSTRAINT `image_info_stratums_FK` FOREIGN KEY (`image_sid`) REFERENCES `stratums` (`stratum_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `image_info_users_FK` FOREIGN KEY (`uploader_num`) REFERENCES `users` (`u_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `image_info_check` CHECK ((`ima_start` > 0)),
  CONSTRAINT `image_info_check_1` CHECK ((`ima_end` > 0)),
  CONSTRAINT `image_info_check_2` CHECK ((`ima_depth` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_info`
--

LOCK TABLES `image_info` WRITE;
/*!40000 ALTER TABLE `image_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `p_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目ID',
  `p_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `p_principal` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '项目负责人',
  `p_team` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '项目负责团队',
  `p_ description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目描述',
  PRIMARY KEY (`p_id`),
  KEY `Project_Engineering_FK` (`p_team`) USING BTREE,
  KEY `project_users_FK` (`p_principal`),
  CONSTRAINT `project_engineering_FK` FOREIGN KEY (`p_team`) REFERENCES `engineering` (`engineering_team_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_users_FK` FOREIGN KEY (`p_principal`) REFERENCES `users` (`u_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stratums`
--

DROP TABLE IF EXISTS `stratums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stratums` (
  `stratum_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岩芯ID',
  `stratum_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岩芯名称',
  `stratum_len` decimal(11,3) NOT NULL COMMENT '岩芯长度',
  `stratum_add` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岩芯地址',
  `stratum_pro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属项目',
  `integrity` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'NO' COMMENT '完整性',
  PRIMARY KEY (`stratum_id`),
  UNIQUE KEY `Stratums_UNIQUE` (`stratum_name`),
  KEY `Stratums_Project_FK` (`stratum_pro`) USING BTREE,
  KEY `index_stratums` (`stratum_id`,`stratum_name`,`stratum_len`,`stratum_add`,`stratum_pro`,`integrity`) USING BTREE,
  CONSTRAINT `stratums_project_FK` FOREIGN KEY (`stratum_pro`) REFERENCES `project` (`p_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stratums_check` CHECK ((`stratum_len` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stratums`
--

LOCK TABLES `stratums` WRITE;
/*!40000 ALTER TABLE `stratums` DISABLE KEYS */;
/*!40000 ALTER TABLE `stratums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `u_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录账号',
  `u_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工号',
  `u_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `u_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `u_sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `u_id` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `u_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `u_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `u_et_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属工程队',
  `u_regdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`u_num`),
  UNIQUE KEY `users_unique` (`u_account`),
  KEY `index_users_one` (`u_num`,`u_name`,`u_sex`,`u_email`,`u_tel`,`u_et_name`) USING BTREE,
  KEY `index_users_two` (`u_num`,`u_name`,`u_et_name`) USING BTREE,
  KEY `users_Engineering_FK` (`u_et_name`) USING BTREE,
  CONSTRAINT `users_engineering_FK` FOREIGN KEY (`u_et_name`) REFERENCES `engineering` (`engineering_team_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `users_check` CHECK ((`u_sex` in (_utf8mb4'男',_utf8mb4'女')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xx`
--

DROP TABLE IF EXISTS `xx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xx` (
  `d_id` int NOT NULL COMMENT '段编号',
  `d_start` decimal(10,3) NOT NULL COMMENT '段开始',
  `d_end` decimal(10,3) NOT NULL COMMENT '段开始',
  `d_depth` decimal(10,3) NOT NULL COMMENT '段深度',
  `d_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '段类型',
  `d_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '段图像存储路径',
  PRIMARY KEY (`d_id`),
  CONSTRAINT `xx_check` CHECK ((`d_start` > 0)),
  CONSTRAINT `xx_check_1` CHECK ((`d_end` > 0)),
  CONSTRAINT `xx_check_2` CHECK ((`d_depth` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xx`
--

LOCK TABLES `xx` WRITE;
/*!40000 ALTER TABLE `xx` DISABLE KEYS */;
/*!40000 ALTER TABLE `xx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'geology_db1'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-27  0:40:04










-- ----------------------------------------------------------------------------------------------------------------------------------------------










-- 1、修改岩芯表完整性字段的触发器*（已完成）
-- 正确的触发器

DELIMITER //
CREATE FUNCTION calculate_total_length(current_sid VARCHAR(30))
RETURNS DECIMAL(11,3)
READS SQL DATA
BEGIN
    DECLARE total_length DECIMAL(11,3);
    SELECT SUM(ima_depth) INTO total_length
    FROM image_info
    WHERE image_sid =current_sid;
    RETURN total_length;
END //
DELIMITER ; 

DELIMITER //
CREATE TRIGGER update_Sintegrity_afterinsert_trigger
AFTER INSERT ON image_info
FOR EACH ROW
BEGIN
    DECLARE total_length DECIMAL(11,3);
    SET total_length=calculate_total_length(NEW.image_sid); 
    IF total_length=(SELECT stratum_len FROM stratums WHERE stratum_id=NEW.image_sid) THEN
        UPDATE stratums
        SET integrity='YES'
        WHERE stratum_id=NEW.image_sid;
END IF;
END //
DELIMITER ;   


DELIMITER //
CREATE TRIGGER update_Sintegrity_afterupdate_trigger
AFTER UPDATE ON image_info
FOR EACH ROW
BEGIN
DECLARE total_length DECIMAL(11,3);
DECLARE INTE CHAR(4);
SELECT integrity INTO INTE FROM stratums WHERE stratum_id=NEW.image_sid;
    SET total_length=calculate_total_length(NEW.image_sid); 
    IF total_length!=(SELECT stratum_len FROM stratums WHERE stratum_id=NEW.image_sid) AND INTE= 'YES'  THEN
        UPDATE stratums
        SET integrity= 'NO'
        WHERE stratum_id=NEW.image_sid;
ELSEIF total_length=(SELECT stratum_len FROM stratums WHERE stratum_id=NEW.image_sid) THEN
   UPDATE stratums
   SET integrity= 'YES'
   WHERE stratum_id=NEW.image_sid;
END IF;
END //
DELIMITER ;   






-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 2、自动创建表的存储过程*
-- 创建存储过程
DELIMITER //  

CREATE PROCEDURE create_stratum_table(IN stratum_id_val VARCHAR(30))  
BEGIN  
  DECLARE table_name VARCHAR(50);  
  DECLARE exists_count INT;  
  SET table_name = CONCAT(stratum_id_val,'_stratum');  
    
  -- 检查表是否存在  
  SET @table_exists_sql = CONCAT('SELECT COUNT(*) INTO @exists_count FROM information_schema.tables WHERE table_schema=DATABASE() AND table_name=\'', table_name, '\'');  
  PREPARE stmt FROM @table_exists_sql;  
  EXECUTE stmt;  
  DEALLOCATE PREPARE stmt;  
    
  -- 如果表不存在，则创建表  
  IF @exists_count = 0 THEN  
    SET @create_table_sql = CONCAT('CREATE TABLE ', table_name, ' (  
      d_id VARCHAR(50) PRIMARY KEY,  
      d_start DECIMAL(10,3) NOT NULL CHECK(d_start>0),  
      d_end DECIMAL(10,3) NOT NULL CHECK(d_end>0),  
      d_depth DECIMAL(10,3) NOT NULL CHECK(d_depth>0),  
      d_type VARCHAR(50) NOT NULL,  
      d_path VARCHAR(100) NOT NULL,  
      FOREIGN KEY(d_id) REFERENCES image_info(image_name)  
    )');  
    PREPARE stmt FROM @create_table_sql;  
    EXECUTE stmt;  
    DEALLOCATE PREPARE stmt;  
      
    -- 设置权限：给用户角色授予SELECT权限，给管理员角色授予所有权限  
    SET @grant_select_to_user = CONCAT('GRANT SELECT ON ', DATABASE(), '.', table_name, ' TO \'user_r\'@\'%\';');  
    SET @grant_all_to_admin = CONCAT('GRANT ALL PRIVILEGES ON ', DATABASE(), '.', table_name, ' TO \'admini_r\'@\'%\';');  
      
    PREPARE stmt_grant_select FROM @grant_select_to_user;  
    EXECUTE stmt_grant_select;  
    DEALLOCATE PREPARE stmt_grant_select;  
      
    PREPARE stmt_grant_all FROM @grant_all_to_admin;  
    EXECUTE stmt_grant_all;  
    DEALLOCATE PREPARE stmt_grant_all;  
      
    -- 刷新权限，使得权限设置立即生效  
    FLUSH PRIVILEGES;  
  END IF;  
      
  IF @exists_count = 0 OR (SELECT COUNT(*) FROM information_schema.table_constraints WHERE table_schema = DATABASE() AND table_name = table_name AND constraint_name = 'PRIMARY') > 0 THEN  
    SET @insert_sql = CONCAT('INSERT INTO ', table_name, '(d_id, d_start, d_end, d_depth, d_type, d_path)',
      'SELECT image_name, ima_start, ima_end, ima_depth, s_type, image_path ',
      'FROM image_info ',
      'WHERE image_sid = \'', stratum_id_val, '\''); 
      -- SELECT image_name, ima_start, ima_end, ima_depth, s_type, image_path  
      -- FROM image_info  
      -- WHERE image_sid =stratum_id_val);  
    PREPARE stmt FROM @insert_sql;  
    EXECUTE stmt;  
    DEALLOCATE PREPARE stmt;  
  END IF;  
END //  
DELIMITER ;


DELIMITER //
-- 每当用户删除图像信息时，就调用这个函数
CREATE PROCEDURE drop_stratum_table(stratum_id_val VARCHAR(30))
BEGIN
DECLARE table_name VARCHAR(50);
SET table_name=CONCAT(stratum_id_val,'_stratum');
SET @table_exists_sql=CONCAT('SELECT COUNT(*) INTO @table_exists FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = \'', table_name, '\'');
PREPARE stmt FROM @table_exists_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
IF @table_exists = 1 THEN
SET @drop_table_sql = CONCAT('DROP TABLE ', table_name);
PREPARE stmt FROM @drop_table_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
END //
DELIMITER ;
-- 这里不能使用触发器来触发自动创建表的存储过程，因为存储过程中含有动态SQL（CREATE）





-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 3、自己只能修改、删除自己上传的图像信息*（已完成）
-- 用触发器来实现：
-- 创建存储过程，当用户尝试更新图像信息表时，会检查该用户是否是该图像信息的所有者或者管理员。
DELIMITER //  
CREATE TRIGGER image_before_update
BEFORE UPDATE ON image_info
FOR EACH ROW
BEGIN
  CALL check_user_update_permission(OLD.image_id);
END //
DELIMITER ;


DELIMITER //  
CREATE PROCEDURE check_user_update_permission(IMA INT)  
BEGIN  
  DECLARE user_ID INT ;  
  DECLARE CUR_USER VARCHAR(50);
  SELECT SUBSTRING_INDEX(USER(), '@', 1) INTO CUR_USER;
  SET @current_user_id = (SELECT u_num FROM users WHERE u_account= CUR_USER);  
IF CUR_USER!='root' THEN
IF CUR_USER!='hhu_sang' THEN
IF CUR_USER!='yangy'  THEN
SET @is_admin = (SELECT COUNT(*) FROM administrators WHERE a_account= CUR_USER);
IF @is_admin = 0 THEN
Select count(*) INTO user_ID from image_info where uploader_num=@current_user_id AND image_id=IMA;
IF user_ID=0 then
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You do not have permission to modify this image.';  
END IF;
END IF;
END IF;
END IF;
END IF;
END //  
DELIMITER ;


DELIMITER //  
CREATE TRIGGER image_before_delete
BEFORE DELETE ON image_info
FOR EACH ROW
BEGIN
  CALL check_user_delete_permission(OLD.image_id);
END //
DELIMITER ;

DELIMITER //  
CREATE PROCEDURE check_user_delete_permission(IMA INT)  
BEGIN  
  DECLARE user_ID INT ;  
  DECLARE CUR_USER VARCHAR(50);
  SELECT SUBSTRING_INDEX(USER(), '@', 1) INTO CUR_USER;
SET @current_user_id = (SELECT u_num FROM users WHERE u_account=CUR_USER);  
IF CUR_USER!='root' THEN
IF CUR_USER!='hhu_sang' THEN
IF CUR_USER!='yangy'  THEN
SET @is_admin = (SELECT COUNT(*) FROM administrators WHERE a_account=CUR_USER);
IF @is_admin = 0 THEN
    SELECT COUNT(*) INTO user_ID FROM image_info WHERE uploader_num = @current_user_id AND image_id = IMA;  
    IF user_ID = 0 THEN  
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You do not have permission to delete this image.';  
END IF;
END IF;
END IF;
END IF;
END IF;
END //  
DELIMITER ;

-- 注意MYSQL中USER()和CURRENT_USER()的区别；

-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 4、密码加密（用触发器来实现，当新插入一个用户信息时就加密、更新密码时加密）*
DELIMITER //  
CREATE TRIGGER encrypt_user_pwd_insert  
BEFORE INSERT ON users  
FOR EACH ROW  
BEGIN  
    SET @encryption_key = 'my_secure_encryption_key';  
SET NEW.u_password= TO_BASE64(AES_ENCRYPT(NEW.u_password, @encryption_key));
END //  
DELIMITER ;


DELIMITER //  
CREATE TRIGGER encrypt_user_pwd_update  
BEFORE UPDATE ON users  
FOR EACH ROW    
BEGIN  
    DECLARE encryption_key VARCHAR(255) DEFAULT 'my_secure_encryption_key';  
    IF NEW.u_password <> OLD.u_password THEN  
        SET NEW.u_password = TO_BASE64(AES_ENCRYPT(NEW.u_password, encryption_key));  
    END IF;  
END //  
DELIMITER ;

DELIMITER //  
CREATE TRIGGER encrypt_admini_pwd_insert  
BEFORE INSERT ON administrators  
FOR EACH ROW  
BEGIN  
    SET @encryption_key = 'my_secure_encryption_key';  
    SET NEW.a_password = TO_BASE64(AES_ENCRYPT(NEW.a_password, @encryption_key));  
END //  
DELIMITER ;

DELIMITER //  
CREATE TRIGGER encrypt_admini_pwd_update 
BEFORE UPDATE ON administrators  
FOR EACH ROW  
BEGIN  
    SET @encryption_key = 'my_secure_encryption_key';    
    IF NEW.a_password <> OLD.a_password THEN  
        SET NEW.a_password = TO_BASE64(AES_ENCRYPT(NEW.a_password, @encryption_key));  
END IF;
END //  
DELIMITER ;

-- 注意，解密后的数据仍然是二进制类型，可以在应用层来完成转换




-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 5、自己可以修改自己的密码、相关信息*（已完成）
-- （用户账号不能修改）

-- 创建触发器：（修改信息）
DELIMITER //
CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    CALL check_user_update(OLD.u_account);
END //
DELIMITER ;


DELIMITER //  
CREATE PROCEDURE check_user_update(u_ac VARCHAR(50))  
BEGIN
DECLARE user_ID INT; 
DECLARE CUR_USER VARCHAR(50);
  SELECT SUBSTRING_INDEX(USER(), '@', 1) INTO CUR_USER;
SET @current_user_id = (SELECT u_num FROM users WHERE u_account=CUR_USER);  
IF CUR_USER!='hhu_sang' THEN
IF CUR_USER!='yangy'  THEN
IF CUR_USER!='root'  THEN
SET @is_admin = (SELECT COUNT(*) FROM administrators WHERE a_account=CUR_USER);  
IF @is_admin = 0 THEN  
IF u_ac!=CUR_USER THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You can only update your own record'; 
END IF;
END IF;
END IF;
END IF;
END IF;
END //  
DELIMITER ;




-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 6、视图（已完成）
-- （1）上传者视图（用于限制用户对上传者信息的访问）用户要是想查看上传者的信息，就查询视图
CREATE VIEW UPLOADER_Image AS  
SELECT DISTINCT u.u_name,u.u_num,u.u_sex,u.u_email,u.u_tel, u.u_et_name
FROM users u  
JOIN image_info ima ON u.u_num= ima.uploader_num  ORDER BY u.u_num;  
-- 使用DISTINCT是为了确保每个用户只出现一次，即使他们上传了多张图片。  

-- （2）管理员视图（为后期联系管理员做准备）
CREATE VIEW ADMINI_INFO AS  
SELECT DISTINCT ad.a_name,ad.a_tel,ad.a_email,ad.a_level
FROM administrators ad;

-- （3）工程队员工视图
CREATE VIEW ET_STAFF AS  
SELECT u.u_et_name,u.u_name,u.u_num
FROM users u  
JOIN engineering ET ON u.u_et_name= ET.engineering_team_name  ORDER BY u.u_et_name;  





-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- 存在则无需运行
-- 7、索引（已完成）:
-- 索引1：在图像信息表上：图像编号、开始、结束、深度、岩芯类型、存储路径 （自动创建表时需要）
-- CREATE INDEX index_image_one ON image_info (image_name,ima_start,ima_end,ima_depth,image_path,s_type);

-- -- 索引2：在图像信息表上:图像名称、图像存储路径、采集者、上传时间、岩芯ID （用于经常查询的字段）
-- Create index index_image_two on image_info(image_name,image_path,upload_time,uploader_num,image_sid);

-- -- 索引3：在用户表上：用户工号、用户姓名、用户性别、用户邮箱、用户联系电话、用户工程队 （主要是为了提高对上传者视图的查询效率）
-- Create index index_users_one on users(u_num,u_name,u_sex,u_email,u_tel,u_et_name);

-- -- 索引4：在用户表上：用户姓名、用户工号、工程队（用于对视图的查询效率优化）
-- Create index index_users_two on users(u_num,u_name,u_et_name);

-- -- 索引5：直接给岩芯表创建一个所有字段索引（岩芯表字段都属于重要信息）
-- Create index index_stratums on stratums(stratum_id,stratum_name,stratum_len,stratum_add,stratum_pro,integrity);


-- DELIMITER //  

-- BEGIN
--     DECLARE CONTINUE HANDLER FOR SQLSTATE '42S21';

--     CREATE INDEX index_image_one ON image_info (image_name,ima_start,ima_end,ima_depth,image_path,s_type);
--     Create index index_image_two on image_info(image_name,image_path,upload_time,uploader_num,image_sid);
--     Create index index_users_one on users(u_num,u_name,u_sex,u_email,u_tel,u_et_name);
--     Create index index_users_two on users(u_num,u_name,u_et_name);
--     Create index index_stratums on stratums(stratum_id,stratum_name,stratum_len,stratum_add,stratum_pro,integrity);

-- END //  
-- DELIMITER ;


-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 9、当一个新用户注册时，调用存储过程权限授予；*（已完成）
-- 得从前端来授权，触发器不支持动态SQL
-- 可以创建存储过程
-- 创建角色：admin_r、user_r;

-- 1、从用户界面注册的授予user_r
DELIMITER //
CREATE PROCEDURE GrantRole_user (IN useraccount VARCHAR(50))
BEGIN
    SET @s = CONCAT('GRANT `user_r` TO \'', useraccount, '\'@\'%\'');
    PREPARE stmt FROM @s;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GrantRole_admin (IN adminaccount VARCHAR(50))
BEGIN
    SET @s = CONCAT('GRANT `admini_r` TO \'', useraccount, '\'@\'%\'');
    PREPARE stmt FROM @s;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;



-- ----------------------------------------------------------------------------------------------------------------------------------------------

-- 11、当用户从前端登录时，前端调用比较函数，将用户输入的用户名和密码与数据库表中的用户名和密码作比较，若相同，则可以登录数据库。
-- 普通用户登录：
DELIMITER //
CREATE FUNCTION compare_userpsw(input_useraccount VARCHAR(255), input_password VARCHAR(255)) 
RETURNS INT READS SQL DATA 
BEGIN 
DECLARE stored_password VARCHAR(255); 
DECLARE encryption_key VARCHAR(255) DEFAULT 'my_secure_encryption_key'; 
DECLARE encrypted_input_password VARCHAR(255); 
DECLARE result INT DEFAULT 0; 

SELECT u_password INTO stored_password FROM users WHERE u_account = input_useraccount; 
SET encrypted_input_password = TO_BASE64(AES_ENCRYPT(input_password, encryption_key)); 

IF encrypted_input_password = stored_password THEN 
SET result = 1; 
END IF; 
RETURN result; 
END //
DELIMITER ;


DELIMITER //
CREATE FUNCTION compare_adminipsw(input_adminiaccount VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci, input_password VARCHAR(255)) 
RETURNS INT READS SQL DATA 
BEGIN 
DECLARE stored_password VARCHAR(255); 
DECLARE encryption_key VARCHAR(255) DEFAULT 'my_secure_encryption_key'; 
DECLARE encrypted_input_password VARCHAR(255); 
DECLARE result INT DEFAULT 0; 
SELECT a_password INTO stored_password FROM administrators WHERE a_account = input_adminiaccount; 
SET encrypted_input_password = TO_BASE64(AES_ENCRYPT(input_password, encryption_key)); 
IF encrypted_input_password = stored_password THEN 
SET result = 1; 
END IF; 
RETURN result; 
END //
DELIMITER ;



-- -- ----------------------------------------------------------------------------------------------------------------------------------------------
-- 插入数据   插入有一定的先后顺序
LOCK TABLES `administrators` WRITE;
/*!40000 ALTER TABLE `administrators` DISABLE KEYS */;
INSERT INTO `administrators` VALUES ('11111111','111','12a','1123','1@edu.cn','110',1),('22222222','111','12b','1123','2@edu.cn','120',2);
/*!40000 ALTER TABLE `administrators` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `engineering` WRITE;
/*!40000 ALTER TABLE `engineering` DISABLE KEYS */;
INSERT INTO `engineering` VALUES ('1',100,'lisi','122'),('2',200,'nike','119');
/*!40000 ALTER TABLE `engineering` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('1','111','111','zhang','男','123','1@qq.com','12321','1','2024-06-26 18:10:11'),('2','222','111','li','女','2323','2@qq.com','133','2','2024-06-26 18:10:11');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES ('1','hhu','111','1','hhu'),('2','nju','222','2','nju');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `stratums` WRITE;
/*!40000 ALTER TABLE `stratums` DISABLE KEYS */;
INSERT INTO `stratums` VALUES ('1','a1',10.000,'china','1','YES'),('2','a2',20.200,'usa','2','NO');
/*!40000 ALTER TABLE `stratums` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `image_info` WRITE;
/*!40000 ALTER TABLE `image_info` DISABLE KEYS */;
INSERT INTO `image_info` VALUES (1,'ice_1','root/1','2024-06-26 18:12:04','111','1',1.000,2.000,'1',10.000),(2,'ice_2','root/2','2024-06-26 18:12:04','222','2',2.000,2.000,'2',20.000);
/*!40000 ALTER TABLE `image_info` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `xx` WRITE;
/*!40000 ALTER TABLE `xx` DISABLE KEYS */;
/*!40000 ALTER TABLE `xx` ENABLE KEYS */;
UNLOCK TABLES;