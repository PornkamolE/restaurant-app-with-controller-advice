-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: db
-- ------------------------------------------------------
-- Server version	5.7.44

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
-- Table structure for table `food_menu`
--

DROP TABLE IF EXISTS `food_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` enum('GRILL','PASTA','SALAD','BEVERAGE') DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_menu`
--

LOCK TABLES `food_menu` WRITE;
/*!40000 ALTER TABLE `food_menu` DISABLE KEYS */;
INSERT INTO `food_menu` VALUES (1,'GRILL','https://www.onceuponachef.com/images/2020/05/best-grilled-chicken-1200x1658.jpg','Grilled Chicken',120),(2,'GRILL','https://goingmywayz.com/wp-content/uploads/2016/08/Honey-Soy-Lacquered-Ribs-1200x675.jpg','BBQ Pork Ribs',150),(3,'PASTA','https://thestayathomechef.com/wp-content/uploads/2020/03/Pasta-Carbonara-2-3-scaled.jpg','Spaghetti Carbonara',100),(4,'PASTA','https://fratellisnewyorkpizza.com/wp-content/uploads/2019/07/pesto-penne-pasta.jpg','Penne Pesto',105),(5,'SALAD','https://www.twopeasandtheirpod.com/wp-content/uploads/2023/04/Caesar-Salad-24-1071x1536.jpg','Caesar Salad',80),(6,'SALAD','https://www.thechunkychef.com/wp-content/uploads/2021/07/Greek-Salad-Recipe-recipe-card.jpg','Greek Salad',85),(7,'BEVERAGE','https://www.tastingtable.com/img/gallery/16-tips-you-need-to-make-the-most-refreshing-summer-lemonade/l-intro-1687228306.jpg','Lemonade',40),(8,'BEVERAGE','https://midwestniceblog.com/wp-content/uploads/2023/07/caramel-iced-coffee-recipe-683x1024.jpg','Iced Coffee',45),(9,'GRILL','https://bigoven-res.cloudinary.com/image/upload/t_recipe-1280/grilled-salmon-3f985f.jpg','Grilled Salmon',189);
/*!40000 ALTER TABLE `food_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` enum('GRILL','PASTA','SALAD','BEVERAGE') DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `status` enum('NEW','COOKING','READY','SERVED') DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,'BEVERAGE','Iced Coffee',45,'SERVED',1),(2,'GRILL','BBQ Pork Ribs',150,'SERVED',2),(3,'GRILL','Grilled Chicken',120,'SERVED',3);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `status` enum('NEW','PROCESSING','READY','DONE') DEFAULT NULL,
  `table_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2025-05-23 05:30:26.410517','DONE',1),(2,'2025-05-23 08:31:39.822102','DONE',2),(3,'2025-05-23 09:16:10.018460','DONE',3);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tables`
--

DROP TABLE IF EXISTS `tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tables` (
  `table_number` int(11) NOT NULL,
  `status` enum('AVAILABLE','OCCUPIED','COMPLETED') DEFAULT NULL,
  PRIMARY KEY (`table_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tables`
--

LOCK TABLES `tables` WRITE;
/*!40000 ALTER TABLE `tables` DISABLE KEYS */;
INSERT INTO `tables` VALUES (1,'AVAILABLE'),(2,'AVAILABLE'),(3,'AVAILABLE'),(4,'AVAILABLE'),(5,'AVAILABLE'),(6,'AVAILABLE'),(7,'AVAILABLE');
/*!40000 ALTER TABLE `tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER','MANAGER','WAITER','CHEF_GRILL','CHEF_PASTA','CHEF_SALAD','CHEF_BEVERAGE') NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '','$2a$10$E5uv1A8Me5tX8Uf6yGzM/.XGyGJJH4FqWPVdNaYIeVGUD3ge.KHPO','MANAGER','PornkamolE'),(2,_binary '','$2a$10$xIuetSGwWUl.gaeEeI0WEuarOIYWjiaERIr2niSmSVjk8bUzWb3wy','CHEF_GRILL','OdewT'),(3,_binary '','$2a$10$B.UOyQMsCuR4ukfUCi0YE.r1UID8ozPZ01Bk1I7cCWD7jtskBdP7.','CHEF_BEVERAGE','BaristaDew'),(4,_binary '','$2a$10$V9NbowuQZHJ60GxOdQ0PuOGzYJKcfEYku/RuL4DOwoFSyRHwH.SwW','WAITER','waitress01');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-23 17:46:40
