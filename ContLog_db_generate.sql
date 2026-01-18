-- Содание БД 'cl'
CREATE DATABASE IF NOT EXISTS cont;
USE cont;

-- Содание таблицы 'users'
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `cod` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
);

-- Содание таблицы 'roles'
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Содание таблицы 'user-roles'
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY(`role_id`),
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

-- Содание таблицы 'carrier'
CREATE TABLE IF NOT EXISTS `carrier` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Содание таблицы 'driver'
CREATE TABLE IF NOT EXISTS `driver` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `car` varchar(255) NOT NULL,
  `carnum` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `patronymic` varchar(255) NOT NULL,
  `surename` varchar(255) NOT NULL,
  `trailer` varchar(255) NOT NULL,
  `trailernum` varchar(255) NOT NULL,
  `carrier_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`carrier_id`) REFERENCES `carrier` (`id`)
);

-- Содание таблицы 'contowner'
CREATE TABLE IF NOT EXISTS `contowner` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Содание таблицы 'container'
CREATE TABLE IF NOT EXISTS `container` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `maxweight` int DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `weighttare` int DEFAULT NULL,
  `contowner_id` bigint NOT NULL,
  `place` varchar(1) DEFAULT NULL,
  `place_id` bigint DEFAULT NULL,
  `w` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`contowner_id`) REFERENCES `contowner` (`id`)
);

-- Содание таблицы 'terminal'
CREATE TABLE IF NOT EXISTS `terminal` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `navi` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Содание таблицы 'sale'
CREATE TABLE IF NOT EXISTS `sale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date_time` datetime(6) DEFAULT NULL,
  `from_address` varchar(255) DEFAULT NULL,
  `from_date` date DEFAULT NULL,
  `from_time` time(6) DEFAULT NULL,
  `manager` varchar(255) DEFAULT NULL,
  `to_address` varchar(255) DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `to_time` time(6) DEFAULT NULL,
  `finished` bit(1) DEFAULT NULL,
  `started` bit(1) DEFAULT NULL,
  `demo_time` time(6) DEFAULT NULL,
  `container_id` bigint DEFAULT NULL,
  `contowner_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`container_id`),
  KEY (`contowner_id`),
  CONSTRAINT FOREIGN KEY (`contowner_id`) REFERENCES `contowner` (`id`),
  CONSTRAINT FOREIGN KEY (`container_id`) REFERENCES `container` (`id`)
);

-- Содание таблицы 'containerdrs'
CREATE TABLE IF NOT EXISTS `containerdrs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_from` date DEFAULT NULL,
  `date_reg` datetime(6) DEFAULT NULL,
  `date_to` date DEFAULT NULL,
  `drs` varchar(1) DEFAULT NULL,
  `io` int DEFAULT NULL,
  `relis` varchar(255) DEFAULT NULL,
  `type_drs` int DEFAULT NULL,
  `w` int DEFAULT NULL,
  `weightb` int DEFAULT NULL,
  `weightn` int DEFAULT NULL,
  `weightt` int DEFAULT NULL,
  `container_id` bigint DEFAULT NULL,
  `driver_id` bigint DEFAULT NULL,
  `fromterminal_id` bigint DEFAULT NULL,
  `sale_id` bigint NOT NULL,
  `toterminal_id` bigint DEFAULT NULL,
  `contowner_id` bigint DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `finished` bit(1) NOT NULL,
  `started` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`container_id`),
  KEY (`driver_id`),
  KEY (`fromterminal_id`),
  KEY (`sale_id`),
  KEY (`toterminal_id`),
  KEY (`contowner_id`),
  KEY (`carrier_id`),
  CONSTRAINT FOREIGN KEY (`contowner_id`) REFERENCES `contowner` (`id`),
  CONSTRAINT FOREIGN KEY (`fromterminal_id`) REFERENCES `terminal` (`id`),
  CONSTRAINT FOREIGN KEY (`toterminal_id`) REFERENCES `terminal` (`id`),
  CONSTRAINT FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`),
  CONSTRAINT FOREIGN KEY (`carrier_id`) REFERENCES `carrier` (`id`),
  CONSTRAINT FOREIGN KEY (`container_id`) REFERENCES `container` (`id`),
  CONSTRAINT FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`)
);
