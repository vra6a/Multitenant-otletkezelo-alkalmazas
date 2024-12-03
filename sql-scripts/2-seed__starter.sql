-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: default_tenant
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `default_tenant`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `default_tenant` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `default_tenant`;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `is_edited` bit(1) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpo553b3rappx4h6o9lb6lr7xy` (`idea_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpo553b3rappx4h6o9lb6lr7xy` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_likes`
--

DROP TABLE IF EXISTS `comment_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_likes` (
  `comment_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKgtjsp4k7rsoon6lnxjjx7cnqp` (`user_id`),
  KEY `FKd0epu3dcjc57pwe7lt5jgfqsi` (`comment_id`),
  CONSTRAINT `FKd0epu3dcjc57pwe7lt5jgfqsi` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKgtjsp4k7rsoon6lnxjjx7cnqp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_likes`
--

LOCK TABLES `comment_likes` WRITE;
/*!40000 ALTER TABLE `comment_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea`
--

DROP TABLE IF EXISTS `idea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq1fshkaxm10vbry80i87x485u` (`idea_box_id`),
  KEY `FKcbrpauo6w2avi3eoaqtfpoxov` (`user_id`),
  CONSTRAINT `FKcbrpauo6w2avi3eoaqtfpoxov` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKq1fshkaxm10vbry80i87x485u` FOREIGN KEY (`idea_box_id`) REFERENCES `idea_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea`
--

LOCK TABLES `idea` WRITE;
/*!40000 ALTER TABLE `idea` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_box`
--

DROP TABLE IF EXISTS `idea_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_box` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `is_sclosed` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfl3suoghrss86b0d8hpy9plmt` (`user_id`),
  CONSTRAINT `FKfl3suoghrss86b0d8hpy9plmt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_box`
--

LOCK TABLES `idea_box` WRITE;
/*!40000 ALTER TABLE `idea_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_juries`
--

DROP TABLE IF EXISTS `idea_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_juries` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK3ib0hproe0he1nfbbyu3mt4w4` (`user_id`),
  KEY `FK7al5i6mntp3l74kunqbq9hanj` (`idea_id`),
  CONSTRAINT `FK3ib0hproe0he1nfbbyu3mt4w4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK7al5i6mntp3l74kunqbq9hanj` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_juries`
--

LOCK TABLES `idea_juries` WRITE;
/*!40000 ALTER TABLE `idea_juries` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_likes`
--

DROP TABLE IF EXISTS `idea_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_likes` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKk6ogm4kd7bdd7kjk9qkv9n4oc` (`user_id`),
  KEY `FK3nooi3gnrtblxgajdytj3ikcw` (`idea_id`),
  CONSTRAINT `FK3nooi3gnrtblxgajdytj3ikcw` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKk6ogm4kd7bdd7kjk9qkv9n4oc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_likes`
--

LOCK TABLES `idea_likes` WRITE;
/*!40000 ALTER TABLE `idea_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_tags`
--

DROP TABLE IF EXISTS `idea_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_tags` (
  `idea_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  KEY `FKcw5ikpmc6lgu7ykgj2u30suuc` (`tag_id`),
  KEY `FK3gt86actmlp7683x3buwfakb4` (`idea_id`),
  CONSTRAINT `FK3gt86actmlp7683x3buwfakb4` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKcw5ikpmc6lgu7ykgj2u30suuc` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_tags`
--

LOCK TABLES `idea_tags` WRITE;
/*!40000 ALTER TABLE `idea_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `required_juries`
--

DROP TABLE IF EXISTS `required_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `required_juries` (
  `idea_box_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKl25fs18lltuf8n869hwir4ki2` (`user_id`),
  KEY `FKbvc217u6gcao0124upfc2k692` (`idea_box_id`),
  CONSTRAINT `FKbvc217u6gcao0124upfc2k692` FOREIGN KEY (`idea_box_id`) REFERENCES `idea_box` (`id`),
  CONSTRAINT `FKl25fs18lltuf8n869hwir4ki2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `required_juries`
--

LOCK TABLES `required_juries` WRITE;
/*!40000 ALTER TABLE `required_juries` DISABLE KEYS */;
/*!40000 ALTER TABLE `required_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_item`
--

DROP TABLE IF EXISTS `score_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `score` int DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `score_sheet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3y8mqcb3cw70oxv1274jvif6n` (`score_sheet_id`),
  CONSTRAINT `FK3y8mqcb3cw70oxv1274jvif6n` FOREIGN KEY (`score_sheet_id`) REFERENCES `score_sheet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_item`
--

LOCK TABLES `score_item` WRITE;
/*!40000 ALTER TABLE `score_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `score_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_sheet`
--

DROP TABLE IF EXISTS `score_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_sheet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfe1hd7rbguy9339ayvopgmamu` (`idea_id`),
  KEY `FKq7ufumpw2ri4w174ncxvj3s79` (`user_id`),
  KEY `FKkfc65uts5155sorlcfnab7rxw` (`idea_box_id`),
  CONSTRAINT `FKfe1hd7rbguy9339ayvopgmamu` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKkfc65uts5155sorlcfnab7rxw` FOREIGN KEY (`idea_box_id`) REFERENCES `idea_box` (`id`),
  CONSTRAINT `FKq7ufumpw2ri4w174ncxvj3s79` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_sheet`
--

LOCK TABLES `score_sheet` WRITE;
/*!40000 ALTER TABLE `score_sheet` DISABLE KEYS */;
INSERT INTO `score_sheet` VALUES (1,NULL,NULL,NULL),(2,NULL,NULL,NULL),(3,NULL,NULL,NULL),(4,NULL,NULL,NULL),(5,NULL,NULL,NULL),(6,NULL,NULL,NULL),(7,NULL,NULL,NULL);
/*!40000 ALTER TABLE `score_sheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'default_tenant'
--

--
-- Dumping routines for database 'default_tenant'
--

--
-- Current Database: `tenant1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tenant1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tenant1`;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `is_edited` bit(1) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpo553b3rappx4h6o9lb6lr7xy` (`idea_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpo553b3rappx4h6o9lb6lr7xy` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_likes`
--

DROP TABLE IF EXISTS `comment_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_likes` (
  `comment_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKgtjsp4k7rsoon6lnxjjx7cnqp` (`user_id`),
  KEY `FKd0epu3dcjc57pwe7lt5jgfqsi` (`comment_id`),
  CONSTRAINT `FKd0epu3dcjc57pwe7lt5jgfqsi` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKgtjsp4k7rsoon6lnxjjx7cnqp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_likes`
--

LOCK TABLES `comment_likes` WRITE;
/*!40000 ALTER TABLE `comment_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea`
--

DROP TABLE IF EXISTS `idea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq1fshkaxm10vbry80i87x485u` (`idea_box_id`),
  KEY `FKcbrpauo6w2avi3eoaqtfpoxov` (`user_id`),
  CONSTRAINT `FKcbrpauo6w2avi3eoaqtfpoxov` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKq1fshkaxm10vbry80i87x485u` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea`
--

LOCK TABLES `idea` WRITE;
/*!40000 ALTER TABLE `idea` DISABLE KEYS */;
INSERT INTO `idea` VALUES (14,'2024-11-13 17:11:27','ez egy ötlet leírása','APPROVED','demo ötlet 1',11,1),(15,'2024-11-13 17:11:38','ez egy ötlet leírása','DENIED','demo ötlet 2',11,1),(16,'2024-11-13 17:11:49','ez egy ötlet leírása','APPROVED','demo ötlet 3',12,1),(17,'2024-11-13 17:11:58','ez egy ötlet leírása','APPROVED','demo ötlet 4',12,1),(18,'2024-11-20 19:11:19','We need new chairs for the office, because the old ones are very used and unconfortable.','APPROVED','New Chairs',13,1),(19,'2024-11-20 19:11:52','The old ones became unstable and some of the are even cracked.','DENIED','New Desks',13,1),(20,'2024-11-20 19:12:33','The old coffee machine has a lot of broblems and sometimes doesn\'t even turn on.','APPROVED','New coffee machine',13,1);
/*!40000 ALTER TABLE `idea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_juries`
--

DROP TABLE IF EXISTS `idea_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_juries` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK3ib0hproe0he1nfbbyu3mt4w4` (`user_id`),
  KEY `FK7al5i6mntp3l74kunqbq9hanj` (`idea_id`),
  CONSTRAINT `FK3ib0hproe0he1nfbbyu3mt4w4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK7al5i6mntp3l74kunqbq9hanj` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_juries`
--

LOCK TABLES `idea_juries` WRITE;
/*!40000 ALTER TABLE `idea_juries` DISABLE KEYS */;
INSERT INTO `idea_juries` VALUES (14,1),(14,3),(15,1),(15,3),(16,1),(16,3),(17,1),(17,3),(18,1),(18,3),(19,1),(19,3),(20,1),(20,3);
/*!40000 ALTER TABLE `idea_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_likes`
--

DROP TABLE IF EXISTS `idea_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_likes` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKk6ogm4kd7bdd7kjk9qkv9n4oc` (`user_id`),
  KEY `FK3nooi3gnrtblxgajdytj3ikcw` (`idea_id`),
  CONSTRAINT `FK3nooi3gnrtblxgajdytj3ikcw` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKk6ogm4kd7bdd7kjk9qkv9n4oc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_likes`
--

LOCK TABLES `idea_likes` WRITE;
/*!40000 ALTER TABLE `idea_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_tags`
--

DROP TABLE IF EXISTS `idea_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_tags` (
  `idea_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  KEY `FKcw5ikpmc6lgu7ykgj2u30suuc` (`tag_id`),
  KEY `FK3gt86actmlp7683x3buwfakb4` (`idea_id`),
  CONSTRAINT `FK3gt86actmlp7683x3buwfakb4` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKcw5ikpmc6lgu7ykgj2u30suuc` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_tags`
--

LOCK TABLES `idea_tags` WRITE;
/*!40000 ALTER TABLE `idea_tags` DISABLE KEYS */;
INSERT INTO `idea_tags` VALUES (14,3),(15,3),(16,3),(17,3),(18,4),(19,4),(20,4);
/*!40000 ALTER TABLE `idea_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ideabox`
--

DROP TABLE IF EXISTS `ideabox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ideabox` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `is_sclosed` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfl3suoghrss86b0d8hpy9plmt` (`user_id`),
  CONSTRAINT `FKfl3suoghrss86b0d8hpy9plmt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ideabox`
--

LOCK TABLES `ideabox` WRITE;
/*!40000 ALTER TABLE `ideabox` DISABLE KEYS */;
INSERT INTO `ideabox` VALUES (11,'demo','2024-11-13 00:00:00',_binary '','Demo Ötletdoboz 1','2024-11-05 00:00:00',1),(12,'demo 2','2024-11-13 00:00:00',_binary '','Demo Ötletdoboz 2','2024-11-12 00:00:00',1),(13,'Tell us how would you upgrade the office! ','2024-11-20 00:00:00',_binary '','Office Upgrades','2024-11-19 00:00:00',1);
/*!40000 ALTER TABLE `ideabox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `required_juries`
--

DROP TABLE IF EXISTS `required_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `required_juries` (
  `idea_box_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKl25fs18lltuf8n869hwir4ki2` (`user_id`),
  KEY `FKbvc217u6gcao0124upfc2k692` (`idea_box_id`),
  CONSTRAINT `FKbvc217u6gcao0124upfc2k692` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
  CONSTRAINT `FKl25fs18lltuf8n869hwir4ki2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `required_juries`
--

LOCK TABLES `required_juries` WRITE;
/*!40000 ALTER TABLE `required_juries` DISABLE KEYS */;
INSERT INTO `required_juries` VALUES (11,1),(11,3),(12,1),(12,3),(13,1),(13,3);
/*!40000 ALTER TABLE `required_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_item`
--

DROP TABLE IF EXISTS `score_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `score` int DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `score_sheet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3y8mqcb3cw70oxv1274jvif6n` (`score_sheet_id`),
  CONSTRAINT `FK3y8mqcb3cw70oxv1274jvif6n` FOREIGN KEY (`score_sheet_id`) REFERENCES `score_sheet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_item`
--

LOCK TABLES `score_item` WRITE;
/*!40000 ALTER TABLE `score_item` DISABLE KEYS */;
INSERT INTO `score_item` VALUES (37,NULL,NULL,'demo szempont 1','STAR',21),(38,NULL,NULL,'demo szempont 2','STAR',21),(39,NULL,NULL,'demo szempont 3','SLIDER',21),(40,NULL,NULL,'demo szempont 1','SLIDER',22),(41,NULL,NULL,'demo szempont 2','SLIDER',22),(42,NULL,NULL,'demo szempont 3','STAR',22),(43,4,NULL,'demo szempont 1','STAR',23),(44,2,NULL,'demo szempont 2','STAR',23),(45,7,NULL,'demo szempont 3','SLIDER',23),(46,5,NULL,'demo szempont 1','STAR',24),(47,2,NULL,'demo szempont 2','STAR',24),(48,3,NULL,'demo szempont 3','SLIDER',24),(49,1,NULL,'demo szempont 1','STAR',25),(50,5,NULL,'demo szempont 2','STAR',25),(51,10,NULL,'demo szempont 3','SLIDER',25),(52,3,NULL,'demo szempont 1','STAR',26),(53,1,NULL,'demo szempont 2','STAR',26),(54,5,NULL,'demo szempont 3','SLIDER',26),(55,9,NULL,'demo szempont 1','SLIDER',27),(56,2,NULL,'demo szempont 2','SLIDER',27),(57,5,NULL,'demo szempont 3','STAR',27),(58,8,NULL,'demo szempont 1','SLIDER',28),(59,3,NULL,'demo szempont 2','SLIDER',28),(60,4,NULL,'demo szempont 3','STAR',28),(61,2,NULL,'demo szempont 1','SLIDER',29),(62,8,NULL,'demo szempont 2','SLIDER',29),(63,5,NULL,'demo szempont 3','STAR',29),(64,4,NULL,'demo szempont 1','SLIDER',30),(65,10,NULL,'demo szempont 2','SLIDER',30),(66,5,NULL,'demo szempont 3','STAR',30),(67,NULL,NULL,'Price','SLIDER',31),(68,NULL,NULL,'Usefulness','STAR',31),(69,NULL,NULL,'Effect on employees','SLIDER',31),(70,NULL,NULL,'Feasibility','STAR',31),(71,7,NULL,'Price','SLIDER',32),(72,5,NULL,'Usefulness','STAR',32),(73,7,NULL,'Effect on employees','SLIDER',32),(74,4,NULL,'Feasibility','STAR',32),(75,10,NULL,'Price','SLIDER',33),(76,3,NULL,'Usefulness','STAR',33),(77,9,NULL,'Effect on employees','SLIDER',33),(78,3,NULL,'Feasibility','STAR',33),(79,4,NULL,'Price','SLIDER',34),(80,5,NULL,'Usefulness','STAR',34),(81,10,NULL,'Effect on employees','SLIDER',34),(82,5,NULL,'Feasibility','STAR',34),(83,7,NULL,'Price','SLIDER',35),(84,3,NULL,'Usefulness','STAR',35),(85,5,NULL,'Effect on employees','SLIDER',35),(86,3,NULL,'Feasibility','STAR',35),(87,9,NULL,'Price','SLIDER',36),(88,2,NULL,'Usefulness','STAR',36),(89,8,NULL,'Effect on employees','SLIDER',36),(90,1,NULL,'Feasibility','STAR',36),(91,1,NULL,'Price','SLIDER',37),(92,4,NULL,'Usefulness','STAR',37),(93,9,NULL,'Effect on employees','SLIDER',37),(94,5,NULL,'Feasibility','STAR',37);
/*!40000 ALTER TABLE `score_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_sheet`
--

DROP TABLE IF EXISTS `score_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_sheet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfe1hd7rbguy9339ayvopgmamu` (`idea_id`),
  KEY `FKq7ufumpw2ri4w174ncxvj3s79` (`user_id`),
  KEY `FKkfc65uts5155sorlcfnab7rxw` (`idea_box_id`),
  CONSTRAINT `FKfe1hd7rbguy9339ayvopgmamu` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKkfc65uts5155sorlcfnab7rxw` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
  CONSTRAINT `FKq7ufumpw2ri4w174ncxvj3s79` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_sheet`
--

LOCK TABLES `score_sheet` WRITE;
/*!40000 ALTER TABLE `score_sheet` DISABLE KEYS */;
INSERT INTO `score_sheet` VALUES (21,NULL,1,11),(22,NULL,1,12),(23,14,1,NULL),(24,15,1,NULL),(25,14,3,NULL),(26,15,3,NULL),(27,16,3,NULL),(28,16,1,NULL),(29,17,1,NULL),(30,17,3,NULL),(31,NULL,1,13),(32,18,1,NULL),(33,19,1,NULL),(34,20,1,NULL),(35,18,3,NULL),(36,19,3,NULL),(37,20,3,NULL);
/*!40000 ALTER TABLE `score_sheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'a'),(2,'nice'),(3,'demo tag'),(4,'Upgrade');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'t1admin','t1','admin','$2a$10$gQvW4fRsiRz0OwQud1iKwOfah76yHvmIl5TpOjVrUU8p0n7eMsIV2','ADMIN'),(2,'t1user','t1','user','$2a$10$jpBXnucbc.hMXz3phYtGAOhrdmQLEoJhpm0rT1NWgepuo3JKF39YC','USER'),(3,'t1jury','t1','jury','$2a$10$fOzMu6dZly/t6JX0m7YMkeu/4BUgLRzWBkLaBI4Nerl9lP0OeE.ra','JURY');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'tenant1'
--

--
-- Dumping routines for database 'tenant1'
--

--
-- Current Database: `tenant2`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tenant2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tenant2`;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `is_edited` bit(1) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpo553b3rappx4h6o9lb6lr7xy` (`idea_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpo553b3rappx4h6o9lb6lr7xy` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_likes`
--

DROP TABLE IF EXISTS `comment_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_likes` (
  `comment_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKgtjsp4k7rsoon6lnxjjx7cnqp` (`user_id`),
  KEY `FKd0epu3dcjc57pwe7lt5jgfqsi` (`comment_id`),
  CONSTRAINT `FKd0epu3dcjc57pwe7lt5jgfqsi` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FKgtjsp4k7rsoon6lnxjjx7cnqp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_likes`
--

LOCK TABLES `comment_likes` WRITE;
/*!40000 ALTER TABLE `comment_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea`
--

DROP TABLE IF EXISTS `idea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq1fshkaxm10vbry80i87x485u` (`idea_box_id`),
  KEY `FKcbrpauo6w2avi3eoaqtfpoxov` (`user_id`),
  CONSTRAINT `FKcbrpauo6w2avi3eoaqtfpoxov` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKq1fshkaxm10vbry80i87x485u` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea`
--

LOCK TABLES `idea` WRITE;
/*!40000 ALTER TABLE `idea` DISABLE KEYS */;
INSERT INTO `idea` VALUES (1,'2024-10-23 21:21:45','hfghfgh','SUBMITTED','fghfg',1,1),(2,'2024-10-23 21:21:50','hjkhj','SUBMITTED','hjkhj',1,1);
/*!40000 ALTER TABLE `idea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_juries`
--

DROP TABLE IF EXISTS `idea_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_juries` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK3ib0hproe0he1nfbbyu3mt4w4` (`user_id`),
  KEY `FK7al5i6mntp3l74kunqbq9hanj` (`idea_id`),
  CONSTRAINT `FK3ib0hproe0he1nfbbyu3mt4w4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK7al5i6mntp3l74kunqbq9hanj` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_juries`
--

LOCK TABLES `idea_juries` WRITE;
/*!40000 ALTER TABLE `idea_juries` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_likes`
--

DROP TABLE IF EXISTS `idea_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_likes` (
  `idea_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKk6ogm4kd7bdd7kjk9qkv9n4oc` (`user_id`),
  KEY `FK3nooi3gnrtblxgajdytj3ikcw` (`idea_id`),
  CONSTRAINT `FK3nooi3gnrtblxgajdytj3ikcw` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKk6ogm4kd7bdd7kjk9qkv9n4oc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_likes`
--

LOCK TABLES `idea_likes` WRITE;
/*!40000 ALTER TABLE `idea_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea_tags`
--

DROP TABLE IF EXISTS `idea_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idea_tags` (
  `idea_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  KEY `FKcw5ikpmc6lgu7ykgj2u30suuc` (`tag_id`),
  KEY `FK3gt86actmlp7683x3buwfakb4` (`idea_id`),
  CONSTRAINT `FK3gt86actmlp7683x3buwfakb4` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKcw5ikpmc6lgu7ykgj2u30suuc` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea_tags`
--

LOCK TABLES `idea_tags` WRITE;
/*!40000 ALTER TABLE `idea_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `idea_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ideabox`
--

DROP TABLE IF EXISTS `ideabox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ideabox` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `is_sclosed` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfl3suoghrss86b0d8hpy9plmt` (`user_id`),
  CONSTRAINT `FKfl3suoghrss86b0d8hpy9plmt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ideabox`
--

LOCK TABLES `ideabox` WRITE;
/*!40000 ALTER TABLE `ideabox` DISABLE KEYS */;
INSERT INTO `ideabox` VALUES (1,'asd','2024-10-29 00:00:00',_binary '\0','asd','2024-10-25 00:00:00',1),(2,'fghfgh','2024-10-23 00:00:00',_binary '\0','fghfg','2024-10-14 00:00:00',1);
/*!40000 ALTER TABLE `ideabox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `required_juries`
--

DROP TABLE IF EXISTS `required_juries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `required_juries` (
  `idea_box_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKl25fs18lltuf8n869hwir4ki2` (`user_id`),
  KEY `FKbvc217u6gcao0124upfc2k692` (`idea_box_id`),
  CONSTRAINT `FKbvc217u6gcao0124upfc2k692` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
  CONSTRAINT `FKl25fs18lltuf8n869hwir4ki2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `required_juries`
--

LOCK TABLES `required_juries` WRITE;
/*!40000 ALTER TABLE `required_juries` DISABLE KEYS */;
INSERT INTO `required_juries` VALUES (2,1),(2,3);
/*!40000 ALTER TABLE `required_juries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_item`
--

DROP TABLE IF EXISTS `score_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `score` int DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `score_sheet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3y8mqcb3cw70oxv1274jvif6n` (`score_sheet_id`),
  CONSTRAINT `FK3y8mqcb3cw70oxv1274jvif6n` FOREIGN KEY (`score_sheet_id`) REFERENCES `score_sheet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_item`
--

LOCK TABLES `score_item` WRITE;
/*!40000 ALTER TABLE `score_item` DISABLE KEYS */;
INSERT INTO `score_item` VALUES (1,NULL,NULL,'fdg','STAR',1),(2,NULL,NULL,'dfgdfg','SLIDER',1),(3,NULL,NULL,'hjk,hj','SLIDER',2),(4,NULL,NULL,'bnmbvn','STAR',2);
/*!40000 ALTER TABLE `score_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_sheet`
--

DROP TABLE IF EXISTS `score_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_sheet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idea_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `idea_box_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfe1hd7rbguy9339ayvopgmamu` (`idea_id`),
  KEY `FKq7ufumpw2ri4w174ncxvj3s79` (`user_id`),
  KEY `FKkfc65uts5155sorlcfnab7rxw` (`idea_box_id`),
  CONSTRAINT `FKfe1hd7rbguy9339ayvopgmamu` FOREIGN KEY (`idea_id`) REFERENCES `idea` (`id`),
  CONSTRAINT `FKkfc65uts5155sorlcfnab7rxw` FOREIGN KEY (`idea_box_id`) REFERENCES `ideabox` (`id`),
  CONSTRAINT `FKq7ufumpw2ri4w174ncxvj3s79` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_sheet`
--

LOCK TABLES `score_sheet` WRITE;
/*!40000 ALTER TABLE `score_sheet` DISABLE KEYS */;
INSERT INTO `score_sheet` VALUES (1,NULL,1,1),(2,NULL,1,2);
/*!40000 ALTER TABLE `score_sheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'t2admin','t2','admin','$2a$10$sniOf1X1lNVJAaPwe.DV4OSOu2O4PVPdlXlnd97ai5G/TatZH2bO.','ADMIN'),(2,'t2user','t2','user','$2a$10$aQIC.Fz8rEci4yZG3KQAHO8zI8XTpIt2vjLjtDdJKrMRKS7wMijAi','USER'),(3,'t2jury','t2','jury','$2a$10$qWN26.lAImrsU2vHnNAj8eRniUdywxPkSb6Ea4OkvVwZJUoa.UNKW','JURY');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'tenant2'
--

--
-- Dumping routines for database 'tenant2'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-03 12:36:41
