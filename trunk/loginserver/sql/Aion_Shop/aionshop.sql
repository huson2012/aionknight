
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `aionshop_categories`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_categories`;
CREATE TABLE `aionshop_categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_categories
-- ----------------------------
INSERT INTO `aionshop_categories` VALUES ('1', 'Купоны');
INSERT INTO `aionshop_categories` VALUES ('2', 'Оружия');
INSERT INTO `aionshop_categories` VALUES ('3', 'Броня');
INSERT INTO `aionshop_categories` VALUES ('4', 'Крылья');
INSERT INTO `aionshop_categories` VALUES ('5', 'Костюмы');
INSERT INTO `aionshop_categories` VALUES ('6', 'Головные уборы');
INSERT INTO `aionshop_categories` VALUES ('7', 'Украшения');
INSERT INTO `aionshop_categories` VALUES ('8', 'Манастоуны');
INSERT INTO `aionshop_categories` VALUES ('9', 'Зелья');
INSERT INTO `aionshop_categories` VALUES ('10', 'Бож. камни');
INSERT INTO `aionshop_categories` VALUES ('11', 'Краски');
INSERT INTO `aionshop_categories` VALUES ('12', 'Петомцы');
INSERT INTO `aionshop_categories` VALUES ('13', 'Другое');

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
  `itemDesc` varchar(512) NOT NULL,
  `itemEyecatch` tinyint(1) NOT NULL DEFAULT '0',
  `itemClassRestrict` varchar(255) DEFAULT '',
  `itemServerRestrict` varchar(255) DEFAULT '',
  PRIMARY KEY (`itemUniqueId`),
  KEY `itemCategory` (`itemCategory`),
  CONSTRAINT `aionshop_items_ibfk_1` FOREIGN KEY (`itemCategory`) REFERENCES `aionshop_categories` (`categoryId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_items
-- ----------------------------
INSERT INTO `aionshop_items` VALUES ('1', '169650001', '1', '1', '5', '[Ивент] Купон на изменение внешности', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('2', '169660001', '1', '1', '10', '[Ивент] Купон на изменение пола', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('3', '169670001', '1', '1', '50', '[Ивент] Купон на изменение имени персонажа', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('4', '169680001', '1', '1', '100', '[Ивент] Купон на изменение названия легиона', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('5', '100100717', '1', '2', '20', 'Булава Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('6', '101500737', '1', '2', '20', 'Посох Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('7', '100500736', '1', '2', '20', 'Орб Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('8', '100000945', '1', '2', '20', 'Меч Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('9', '101700760', '1', '2', '20', 'Лук Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('10', '101300692', '1', '2', '20', 'Копье Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('11', '100200845', '1', '2', '20', 'Кинжал Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('12', '100900722', '1', '2', '20', 'Двуручный меч Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('13', '100600793', '1', '2', '20', 'Гримуар Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('14', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('15', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('16', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('17', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('18', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('19', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('20', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('21', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('22', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('23', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('24', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('25', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('26', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('27', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('28', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('29', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('30', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('31', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('32', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('33', '', '1', '3', '', '', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('34', '187000037', '1', '4', '25', 'Перо удачи', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('35', '187050001', '1', '4', '25', '[Ивент] Крылья', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('36', '187050005', '1', '4', '25', '[Ивент] Крылья Даллосинга S402', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('37', '187000032', '1', '4', '25', 'Крылья братства Маркутана', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('38', '187000018', '1', '4', '25', 'Крылья бури', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('39', '187000026', '1', '4', '25', 'Сияющие крылья полководца', '1', '0', '', '');


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_transactions
-- ----------------------------
