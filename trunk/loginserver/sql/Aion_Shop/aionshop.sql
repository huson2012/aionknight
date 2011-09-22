
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
INSERT INTO `aionshop_categories` VALUES ('2', 'Купоны');
INSERT INTO `aionshop_categories` VALUES ('3', 'Оружия');
INSERT INTO `aionshop_categories` VALUES ('4', 'Броня');
INSERT INTO `aionshop_categories` VALUES ('5', 'Крылья');
INSERT INTO `aionshop_categories` VALUES ('6', 'Костюмы');
INSERT INTO `aionshop_categories` VALUES ('7', 'Головные уборы');
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
INSERT INTO `aionshop_items` VALUES ('1', '169650001', '1', '2', '5', '[Ивент] Купон на изменение внешности', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('2', '169660001', '1', '2', '10', '[Ивент] Купон на изменение пола', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('3', '169670001', '1', '2', '50', '[Ивент] Купон на изменение имени персонажа', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('4', '169680001', '1', '2', '100', '[Ивент] Купон на изменение названия легиона', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('5', '100100717', '1', '3', '20', 'Булава Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('6', '101500737', '1', '3', '20', 'Посох Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('7', '100500736', '1', '3', '20', 'Орб Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('8', '100000945', '1', '3', '20', 'Меч Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('9', '101700760', '1', '3', '20', 'Лук Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('10', '101300692', '1', '3', '20', 'Копье Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('11', '100200845', '1', '3', '20', 'Кинжал Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('12', '100900722', '1', '3', '20', 'Двуручный меч Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('13', '100600793', '1', '3', '20', 'Гримуар Вердфольнир', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('14', '110301203', '1', '4', '10', 'Дублет Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('15', '113301172', '1', '4', '10', 'Кожаные набедренники Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('16', '112301092', '1', '4', '10', 'Кожаные наплечи Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('17', '111301146', '1', '4', '10', 'Кожаные перчатки Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('18', '114301203', '1', '4', '10', 'Сапоги Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('19', '110601150', '1', '4', '10', 'Кираса Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('20', '113601110', '1', '4', '10', 'Латные набедренники Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('21', '112601097', '1', '4', '10', 'Латные щитки Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('22', '111601119', '1', '4', '10', 'Латные рукавицы Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('23', '114601103', '1', '4', '10', 'Латные сапоги Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('24', '110501177', '1', '4', '10', 'Кольчуга Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('25', '113501153', '1', '4', '10', 'Набедренники Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('26', '112501085', '1', '4', '10', 'Щитки Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('27', '111501136', '1', '4', '10', 'Рукавицы Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('28', '114501157', '1', '4', '10', 'Кольчужные сапоги Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('29', '110101276', '1', '4', '10', 'Туника Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('30', '113101170', '1', '4', '10', 'Тканые набедренники Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('31', '112101109', '1', '4', '10', 'Наплечники Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('32', '111101151', '1', '4', '10', 'Перчатки Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('33', '114101197', '1', '4', '10', 'Ботинки Сурамы', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('34', '187000037', '1', '5', '25', 'Перо удачи', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('35', '187050001', '1', '5', '25', '[Ивент] Крылья', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('36', '187050005', '1', '5', '25', '[Ивент] Крылья Даллосинга S402', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('37', '187000032', '1', '5', '25', 'Крылья братства Маркутана', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('38', '187000018', '1', '5', '25', 'Крылья бури', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('39', '187000026', '1', '5', '25', 'Сияющие крылья полководца', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('40', '110900223', '1', '6', '50', 'Спортивный костюм', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('41', '110900171', '1', '6', '50', 'Роскошный костюм мага', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('42', '110900138', '1', '6', '50', 'Костюм крутого парня', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('43', '110900212', '1', '6', '50', 'Костюм запретной любви', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('44', '110900111', '1', '6', '50', 'Костюм разлуки', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('45', '125002536', '1', '7', '30', 'Священный обруч верховного магистра арены', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('46', '125002528', '1', '7', '30', 'Священный кожаный шлем верховного магистра арены', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('47', '125002544', '1', '7', '30', 'Священный шлем верховного магистра арены', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('48', '125002552', '1', '7', '30', 'Священный латный шлем верховного магистра арены', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('49', '125002155', '1', '7', '30', 'Прекрасные турмалиновые очки', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('50', '125002105', '1', '7', '30', 'Прекрасные кельфаратовые очки', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('51', '166150004', '100', '8', '20', 'Катализатор маг. камней - 100% (герой)', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('52', '166000120', '100', '8', '10', 'Волшебный камень (ур. 120)', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('53', '166000130', '100', '8', '11', 'Волшебный камень (ур. 130)', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('54', '166000190', '100', '8', '20', 'Волшебный камень (ур. 190)', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('55', '167000518', '100', '8', '20', 'Маг. камень: Атака +5', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('56', '167000551', '100', '8', '20', 'Маг. камень: HP +95', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('57', '167000552', '100', '8', '20', 'Маг. камень: MP +95', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('58', '167000557', '100', '8', '20', 'Маг. камень: Блок щитом +27', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('59', '167000559', '100', '8', '20', 'Маг. камень: Время полета +7', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('60', '167000561', '100', '8', '20', 'Маг. камень: Магическая защита +14', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('61', '167000558', '100', '8', '20', 'Маг. камень: Ф. крит. удар +17', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('62', '167000554', '100', '8', '20', 'Маг. камень: Уклонение +17', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('63', '167000560', '100', '8', '20', 'Маг. камень: Точность магии +14', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('64', '167000553', '100', '8', '20', 'Маг. камень: Точность +27', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('65', '167000555', '100', '8', '20', 'Маг. камень: Сила магии +27', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('66', '167000563', '100', '8', '20', 'Маг. камень: Сила исцеления +3', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('67', '162000069', '100', '9', '50', 'Особое зелье жизни IV', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('68', '162000070', '100', '9', '50', 'Чудесное зелье маны IV', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('69', '164000112', '100', '9', '50', 'Редкое зелье ветра V', '1', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('70', '162000023', '100', '9', '50', 'Сильное зелье исцеления', '1', '0', '', '');

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
