SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `account_data`
-- ----------------------------
DROP TABLE IF EXISTS `account_data`;
CREATE TABLE `account_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `access_level` tinyint(3) NOT NULL DEFAULT '0',
  `membership` tinyint(3) NOT NULL DEFAULT '0',
  `last_server` tinyint(3) NOT NULL DEFAULT '-1',
  `last_ip` varchar(20) DEFAULT NULL,
  `ip_force` varchar(20) DEFAULT NULL,
  `expire` date DEFAULT NULL,
  `credits` bigint(21) NOT NULL DEFAULT '0',
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_data
-- ----------------------------

-- ----------------------------
-- Table structure for `account_time`
-- ----------------------------
DROP TABLE IF EXISTS `account_time`;
CREATE TABLE `account_time` (
  `account_id` int(11) NOT NULL,
  `last_active` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expiration_time` timestamp NULL DEFAULT NULL,
  `session_duration` int(10) DEFAULT '0',
  `accumulated_online` int(10) DEFAULT '0',
  `accumulated_rest` int(10) DEFAULT '0',
  `penalty_end` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_time
-- ----------------------------

-- ----------------------------
-- Table structure for `aionshop_categories`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_categories`;
CREATE TABLE `aionshop_categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_categories
-- ----------------------------
INSERT INTO `aionshop_categories` VALUES ('1', 'Сеты брони');
INSERT INTO `aionshop_categories` VALUES ('2', 'Усиление');
INSERT INTO `aionshop_categories` VALUES ('3', 'Манастоуны');
INSERT INTO `aionshop_categories` VALUES ('4', 'Крылья');
INSERT INTO `aionshop_categories` VALUES ('5', 'Расходники');
INSERT INTO `aionshop_categories` VALUES ('6', 'Книги умений');
INSERT INTO `aionshop_categories` VALUES ('7', 'Бож. камни');
INSERT INTO `aionshop_categories` VALUES ('8', 'Банки');
INSERT INTO `aionshop_categories` VALUES ('9', 'Костюмы');
INSERT INTO `aionshop_categories` VALUES ('10', 'Аксесуары');
INSERT INTO `aionshop_categories` VALUES ('11', 'Краски');
INSERT INTO `aionshop_categories` VALUES ('12', 'Питомцы');
INSERT INTO `aionshop_categories` VALUES ('13', 'Заточки');
INSERT INTO `aionshop_categories` VALUES ('14', 'Другое');

-- ----------------------------
-- Table structure for `aionshop_items`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_items`;
CREATE TABLE `aionshop_items` (
  `itemUniqueId` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemTemplateId` bigint(20) NOT NULL,
  `itemCount` int(11) NOT NULL,
  `itemCategory` int(11) NOT NULL,
  `itemPrice` int(11) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemDesc` varchar(255) NOT NULL,
  `itemEyecatch` tinyint(1) NOT NULL DEFAULT '0',
  `itemClassRestrict` varchar(255) NOT NULL,
  `itemServerRestrict` varchar(255) NOT NULL,
  PRIMARY KEY (`itemUniqueId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_items
-- ----------------------------

-- ----------------------------
-- Table structure for `aionshop_transactions`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_transactions`;
CREATE TABLE `aionshop_transactions` (
  `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_id` int(11) NOT NULL,
  `item_unique_id` int(11) NOT NULL,
  `buy_timestamp` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_transactions
-- ----------------------------

-- ----------------------------
-- Table structure for `banned_ip`
-- ----------------------------
DROP TABLE IF EXISTS `banned_ip`;
CREATE TABLE `banned_ip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mask` varchar(45) NOT NULL,
  `time_end` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mask` (`mask`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of banned_ip
-- ----------------------------

-- ----------------------------
-- Table structure for `gameservers`
-- ----------------------------
DROP TABLE IF EXISTS `gameservers`;
CREATE TABLE `gameservers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mask` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gameservers
-- ----------------------------