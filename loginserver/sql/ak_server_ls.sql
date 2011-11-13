/*
 Source Server         : Aion-Knight RC1 [Beta]
 Source Server Type    : MySQL
 Source Server Version : 50045
 Source Host           : 192.168.0.1
 Source Database       : ak_server_ls

 Target Server Type    : MySQL
 Target Server Version : 50045
 File Encoding         : utf-8

 Date: 04/10/2011 08:33:19 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account_data`
-- ----------------------------
DROP TABLE IF EXISTS `account_data`;
CREATE TABLE `account_data` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `activated` tinyint(1) NOT NULL default '1',
  `access_level` tinyint(3) NOT NULL default '0',
  `membership` tinyint(3) NOT NULL default '0',
  `last_server` tinyint(3) NOT NULL default '-1',
  `last_ip` varchar(20) default NULL,
  `ip_force` varchar(20) default NULL,
  `credits` bigint(21) NOT NULL default '0',
  `email` varchar(30) default NULL,
  `last_logout` bigint(21) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `account_time`
-- ----------------------------
DROP TABLE IF EXISTS `account_time`;
CREATE TABLE `account_time` (
  `account_id` int(11) NOT NULL,
  `last_active` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `expiration_time` timestamp NULL default NULL,
  `session_duration` int(10) default '0',
  `accumulated_online` int(10) default '0',
  `accumulated_rest` int(10) default '0',
  `penalty_end` timestamp NULL default NULL,
  PRIMARY KEY  (`account_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `aionshop_categories`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_categories`;
CREATE TABLE `aionshop_categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY  (`categoryId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `aionshop_categories`
-- ----------------------------
BEGIN;
INSERT INTO `aionshop_categories` VALUES ('3', 'Tickets'), ('4', 'Sets'), ('5', 'Enchants'), ('6', 'Manastones'), ('7', 'Wings'), ('8', 'Consumables'), ('9', 'Weapons'), ('10', 'Godstones'), ('11', 'Foods'), ('12', 'Costumes'), ('13', 'Hat\'s'), ('14', 'Accesories'), ('15', 'Dyes'), ('16', 'Pets'), ('18', 'FUN'), ('19', 'Offerts');
COMMIT;

-- ----------------------------
--  Table structure for `aionshop_items`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_items`;
CREATE TABLE `aionshop_items` (
  `itemUniqueId` bigint(20) NOT NULL auto_increment,
  `itemTemplateId` bigint(20) NOT NULL,
  `itemCount` int(11) NOT NULL,
  `itemCategory` int(11) NOT NULL,
  `itemPrice` int(11) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemDesc` varchar(255) NOT NULL,
  `itemEyecatch` tinyint(1) NOT NULL default '0',
  `itemClassRestrict` varchar(255) NOT NULL,
  `itemServerRestrict` varchar(255) NOT NULL,
  PRIMARY KEY  (`itemUniqueId`)
) ENGINE=MyISAM AUTO_INCREMENT=159 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `aionshop_items`
-- ----------------------------
BEGIN;
INSERT INTO `aionshop_items` VALUES ('6', '169650000', '1', '3', '1000', 'Plastic Surgery', 'Plastic Surgery', '0', '', ''), ('7', '169670000', '1', '3', '1000', 'Name Change', 'Name Change', '0', '', ''), ('8', '169680000', '1', '3', '1000', 'Legion Name Change', 'Legion Name Change', '0', '', ''), ('9', '187000037', '1', '7', '2000', 'Lucky Wings', 'Lucky Wings', '0', '', ''), ('10', '187060007', '1', '7', '1000', 'Garkbinerk\'s Black Cloud Wings', 'Garkbinerk\'s Black Cloud Wings', '0', '', ''), ('11', '187000031', '1', '7', '1500', 'Orichalcum Key Wings', 'Orichalcum Key Wings', '0', '', ''), ('12', '187000018', '1', '7', '1500', 'Storm Wings\r\n', 'Storm Wings', '0', '', ''), ('13', '187000024', '1', '7', '1500', 'Noble Chief Operative\'s Wings', 'Noble Chief Operative\'s Wings', '0', '', ''), ('14', '187000011', '1', '7', '1300', 'Noble Agony Wing Feather (Asmodian)', 'Noble Agony Wing Feather (Asmodian)', '0', '', ''), ('15', '187000010', '1', '7', '1300', 'Haroshunerk\'s Noble Wing Feather (Elyos)\r\n', 'Haroshunerk\'s Noble Wing Feather (Elyos)', '0', '', ''), ('16', '187000007', '1', '7', '600', 'Sigyn\'s Wing Feather ', 'Sigyn\'s Wing Feather ', '0', '', ''), ('17', '187000006', '1', '7', '600', 'Nestor\'s Wing Feather \r\n', 'Nestor\'s Wing Feather \r\n', '0', '', ''), ('18', '166000190', '15', '5', '1000', 'L190 Enchantment Stone (15)', 'L190 Enchantment Stone (15)', '0', '', ''), ('19', '166000150', '15', '5', '800', 'L150 Enchantment Stone (15)', 'L150 Enchantment Stone (15)', '0', '', ''), ('20', '166000140', '15', '5', '600', 'L140 Enchantment Stone (15)\r\n', 'L140 Enchantment Stone (15)\r\n', '0', '', ''), ('21', '167000551', '10', '6', '400', 'Manastone HP + 95 (10)\r\nManastone HP + 95 (10)', 'Manastone HP + 95 (10)', '0', '', ''), ('22', '167000552', '10', '6', '400', 'Manastone MP +95 (10)\r\n', 'Manastone MP +95 (10)\r\n', '0', '', ''), ('23', '167000555', '10', '6', '400', 'Manastone Magic Boost +27 (10)', '', '0', '', ''), ('24', '167000558', '10', '6', '400', 'Manastone Crit Strike +17  (10)\r\nManastone Crit Strike +17  (10)', 'Manastone Crit Strike +17  (10)', '0', '', ''), ('25', '167000554', '10', '6', '400', 'Manastone Evasion +17 (10)', 'Manastone Evasion +17 (10)', '0', '', ''), ('26', '167000557', '10', '6', '400', 'Manastone: Block +27 (10)', 'Manastone: Block +27 (10)', '0', '', ''), ('27', '167000560', '10', '6', '400', 'Manastone: Magical Accuracy +14 (10)', 'Manastone: Magical Accuracy +14 (10)', '0', '', ''), ('28', '167000556', '10', '6', '400', 'Manastone: Parry +27 (10)', 'Manastone: Parry +27 (10)', '0', '', ''), ('29', '167000561', '10', '6', '400', 'Manastone: Resist Magic +14 (10)', '', '0', '', ''), ('30', '168000052', '1', '10', '1000', 'Godstone: Kaisinel\'s Terror (Elyos)', 'Godstone: Kaisinel\'s Terror (Elyos)', '0', '', ''), ('31', '168000056', '1', '10', '1000', 'Godstone: Lumiel\'s Magical Power (Asmodian)', 'Godstone: Lumiel\'s Magical Power (Asmodian)', '0', '', ''), ('32', '168000118', '1', '10', '1000', 'Godstone: Ereshkigal\'s Honor', 'Godstone: Ereshkigal\'s Honor', '0', '', ''), ('33', '168000051', '1', '10', '1000', 'Godstone: Nezekan\'s Valiance (Elyos)', 'Godstone: Nezekan\'s Valiance (Elyos)', '0', '', ''), ('34', '168000058', '1', '10', '1000', 'Godstone: Triniel\'s Coolness (Asmodian)', 'Godstone: Triniel\'s Coolness (Asmodian)', '0', '', ''), ('35', '169220032', '1', '15', '20', 'True Black', 'True Black', '0', '', ''), ('36', '169220028', '1', '15', '20', 'True White', 'True White', '0', '', ''), ('37', '169240002', '1', '15', '20', 'Omblic Petal', 'Omblic Petal', '0', '', ''), ('38', '169240009', '1', '15', '20', 'Kirka Petal', 'Kirka Petal', '0', '', ''), ('39', '169240010', '1', '15', '20', 'Brommel Petal', 'Brommel Petal', '0', '', ''), ('40', '100000934', '1', '9', '2500', 'Archon Brigade General\'s Sword', 'Archon Brigade General\'s Sword', '0', '', ''), ('41', '100000921', '1', '9', '2000', 'Enraged Mastarius\'s Sword', 'Enraged Mastarius\'s Sword', '0', '', ''), ('42', '100000916', '1', '9', '2000', 'Enraged Veille\'s Sword', 'Enraged Veille\'s Sword', '0', '', ''), ('43', '100100704', '1', '9', '2500', 'Archon Brigade General\'s Warhammer\r\n', 'Archon Brigade General\'s Warhammer\r\n', '0', '', ''), ('44', '100100691', '1', '9', '2000', 'Enraged Mastarius\'s Warhammer\r\nEnraged Mastarius\'s Warhammer', 'Enraged Mastarius\'s Warhammer', '0', '', ''), ('45', '100100686', '1', '9', '2000', 'Enraged Veille\'s Warhammer', 'Enraged Veille\'s Warhammer', '0', '', ''), ('46', '100200834', '1', '9', '2500', 'Archon Brigade General\'s Dagger', 'Archon Brigade General\'s Dagger', '0', '', ''), ('47', '100200821', '1', '9', '2000', 'Enraged Mastarius\'s Dagger', 'Enraged Mastarius\'s Dagger', '0', '', ''), ('48', '100200816', '1', '9', '2000', 'Enraged Veille\'s Dagger', 'Enraged Veille\'s Dagger', '0', '', ''), ('49', '100500725', '1', '9', '2500', 'Archon Brigade General\'s Jewel', 'Archon Brigade General\'s Jewel', '0', '', ''), ('50', '100500712', '1', '9', '2000', 'Enraged Mastarius\'s Jewel', 'Enraged Mastarius\'s Jewel', '0', '', ''), ('51', '100500707', '1', '9', '2000', 'Enraged Veille\'s Jewel', 'Enraged Veille\'s Jewel', '0', '', ''), ('52', '100600782', '1', '9', '2500', 'Archon Brigade General\'s Tome', 'Archon Brigade General\'s Tome', '0', '', ''), ('53', '100900711', '1', '9', '2000', 'Enraged Mastarius\'s Tome', 'Enraged Mastarius\'s Tome', '0', '', ''), ('54', '100600764', '1', '9', '2000', 'Enraged Veille\'s Tome', 'Enraged Veille\'s Tome', '0', '', ''), ('55', '100900711', '1', '9', '2500', 'Archon Brigade General\'s Greatsword', 'Archon Brigade General\'s Greatsword', '0', '', ''), ('56', '100900698', '1', '9', '2000', 'Enraged Mastarius\'s Greatsword', 'Enraged Mastarius\'s Greatsword', '0', '', ''), ('57', '100900693', '1', '9', '2000', 'Enraged Veille\'s Greatsword', 'Enraged Veille\'s Greatsword', '0', '', ''), ('58', '101300681', '1', '9', '2500', 'Archon Brigade General\'s Spear', 'Archon Brigade General\'s Spear', '0', '', ''), ('59', '101300668', '1', '9', '2000', 'Enraged Mastarius\'s Spear', 'Enraged Mastarius\'s Spear', '0', '', ''), ('60', '101300663', '1', '9', '2000', 'Enraged Veille\'s Spear', 'Enraged Veille\'s Spear', '0', '', ''), ('61', '101700749', '1', '9', '2500', 'Archon Brigade General\'s Longbow', 'Archon Brigade General\'s Longbow', '0', '', ''), ('62', '101700736', '1', '9', '2000', 'Enraged Mastarius\'s Longbow', 'Enraged Mastarius\'s Longbow', '0', '', ''), ('63', '101700731', '1', '9', '2000', 'Enraged Veille\'s Longbow', 'Enraged Veille\'s Longbow', '0', '', ''), ('64', '101500726', '1', '9', '2500', 'Archon Brigade General\'s Staff', 'Archon Brigade General\'s Staff', '0', '', ''), ('65', '101500713', '1', '9', '2000', 'Enraged Mastarius\'s Staff', 'Enraged Mastarius\'s Staff', '0', '', ''), ('66', '101500707', '1', '9', '2000', 'Enraged Veille\'s Staff', 'Enraged Veille\'s Staff', '0', '', ''), ('67', '115000998', '1', '9', '2500', 'Archon Brigade General\'s Shield', 'Archon Brigade General\'s Shield', '0', '', ''), ('68', '115001018', '1', '9', '2000', 'Enraged Mastarius\'s Shield', 'Enraged Mastarius\'s Shield', '0', '', ''), ('69', '115001016', '1', '9', '2000', 'Enraged Veille\'s Shield', 'Enraged Veille\'s Shield', '0', '', ''), ('70', '162000066', '100', '8', '700', 'Recovery Crystal (100)', 'Recovery Crystal (100)', '0', '', ''), ('71', '162000080', '100', '8', '700', 'Fine Recovery Serum (100)', 'Fine Recovery Serum (100)', '0', '', ''), ('72', '162000077', '100', '8', '600', 'Fine Life Serum (100)', 'Fine Life Serum (100)', '0', '', ''), ('73', '162000081', '100', '8', '700', 'Fine Recovery Potion (100)', 'Fine Recovery Potion (100)', '0', '', ''), ('74', '162000078', '100', '8', '600', 'Fine Mana Serum (100)', 'Fine Mana Serum (100)', '0', '', ''), ('75', '162000069', '100', '8', '700', 'Major Divine Life Serum (100)', 'Major Divine Life Serum (100)', '0', '', ''), ('76', '162000070', '100', '8', '700', 'Major Divine Mana Serum (100)', 'Major Divine Mana Serum (100)', '0', '', ''), ('77', '162000071', '100', '8', '700', 'Major Divine Wind Serum (100)', 'Major Divine Wind Serum (100)', '0', '', ''), ('79', '120001069', '1', '14', '1300', 'Archon Brigade General\'s Corundum Earrings', 'Archon Brigade General\'s Corundum Earrings', '0', '', ''), ('80', '120001070', '1', '14', '1300', 'Archon Brigade General\'s Turquoise Earrings', 'Archon Brigade General\'s Turquoise Earrings', '0', '', ''), ('81', '122001206', '1', '14', '1300', 'Archon Brigade General\'s Corundum Ring', 'Archon Brigade General\'s Corundum Ring', '0', '', ''), ('82', '122001207', '1', '14', '1300', 'Archon Brigade General\'s Turquoise Ring', 'Archon Brigade General\'s Turquoise Ring', '0', '', ''), ('83', '121000979', '1', '14', '1500', 'Archon Brigade General\'s Corundum Necklace', 'Archon Brigade General\'s Corundum Necklace', '0', '', ''), ('84', '121000980', '1', '14', '1500', 'Archon Brigade General\'s Turquoise Necklace', 'Archon Brigade General\'s Turquoise Necklace', '0', '', ''), ('85', '169660000', '1', '3', '1000', 'Gender Switch Ticket', 'Gender Switch Ticket', '0', '', ''), ('86', '164000076', '100', '8', '500', 'Greater Running Scroll (100)', 'Greater Running Scroll (100)', '0', '', ''), ('87', '164000079', '100', '8', '500', 'Greater Raging Wind Scroll (100)', 'Greater Raging Wind Scroll (100)', '0', '', ''), ('88', '164000075', '100', '8', '300', 'Running Scroll (100)', 'Running Scroll (100)', '0', '', ''), ('89', '164000078', '100', '8', '300', 'Raging Wind Scroll (100)', 'Raging Wind Scroll (100)', '0', '', ''), ('90', '190020031', '1', '16', '1000', 'Aqua Griffo', 'Aqua Griffo\r\n', '0', '', ''), ('91', '190020012', '1', '16', '1000', 'Bluehair Steren', 'Bluehair Steren', '0', '', ''), ('92', '190020002', '1', '16', '1000', 'Bottlenose Tion', 'Bottlenose Tion', '0', '', ''), ('93', '190020016', '1', '16', '1000', 'Goldmane Trico', 'Goldmane Trico', '0', '', ''), ('94', '190020006', '1', '16', '1000', 'Jumpy Shugo', 'Jumpy Shugo', '0', '', ''), ('95', '190020022', '1', '16', '1000', 'Mean Poroco', 'Mean Poroco', '0', '', ''), ('96', '190020059', '1', '16', '1000', 'Palmist Acarun', 'Palmist Acarun', '0', '', ''), ('97', '190020024', '1', '16', '1000', 'Azure Drakie', 'Azure Drakie', '0', '', ''), ('98', '190020008', '1', '16', '1000', 'Golden Nyanco', 'Golden Nyanco', '0', '', ''), ('99', '190020049', '1', '16', '1000', 'Dapper Taegeuk Shugo', 'Dapper Taegeuk Shugo', '0', '', ''), ('100', '110900206', '1', '12', '200', 'Banded Party Outfit', 'Banded Party Outfit', '0', '', ''), ('101', '110900205', '1', '12', '200', 'Colorblock Citywear', 'Colorblock Citywear', '0', '', ''), ('102', '110900203', '1', '12', '200', 'One-shoulder Special', 'One-shoulder Special', '0', '', ''), ('103', '110900214', '1', '12', '200', '\"My Other Half\" T-Shirt', '\"My Other Half\" T-Shirt', '0', '', ''), ('104', '110900136', '1', '12', '200', 'Foxtail Hot Pants Outfit', 'Foxtail Hot Pants Outfit', '0', '', ''), ('105', '110900215', '1', '12', '200', 'Couture Clothing', 'Couture Clothing', '0', '', ''), ('106', '110900139', '1', '12', '200', 'Sorcerer\'s Robe', 'Sorcerer\'s Robe', '0', '', ''), ('107', '110900201', '1', '12', '200', 'Sheer Envy Outfit', 'Sheer Envy Outfit', '0', '', ''), ('108', '110900198', '1', '12', '200', 'Red Vine Wrapper', 'Red Vine Wrapper', '0', '', ''), ('109', '110900193', '1', '12', '200', 'Long Sleeved Seduction', 'Long Sleeved Seduction', '0', '', ''), ('110', '110900087', '1', '12', '200', 'Cute Denim Dress', 'Cute Denim Dress', '0', '', ''), ('111', '110900069', '1', '12', '200', 'Raiments of Mastery', 'Raiments of Mastery', '0', '', ''), ('112', '110900111', '1', '12', '200', 'Beatific Outfit', 'Beatific Outfit', '0', '', ''), ('113', '110900050', '1', '12', '200', 'Dynasty Robes', 'Dynasty Robes', '0', '', ''), ('114', '110900049', '1', '12', '200', 'Dynasty Light Armor', 'Dynasty Light Armor', '0', '', ''), ('115', '110900048', '1', '12', '200', 'Dynasty Heavy Armor', 'Dynasty Heavy Armor', '0', '', ''), ('116', '110101099', '1', '4', '1700', 'Archon Brigade General\'s Tunic', 'Archon Brigade General\'s Tunic', '0', '', ''), ('117', '111100999', '1', '4', '1700', 'Archon Brigade General\'s Gloves', 'Archon Brigade General\'s Gloves', '0', '', ''), ('120', '112100955', '1', '4', '1700', 'Archon Brigade General\'s Pauldrons', 'Archon Brigade General\'s Pauldrons', '0', '', ''), ('121', '113101011', '1', '4', '1700', 'Archon Brigade General\'s Leggings', 'Archon Brigade General\'s Leggings', '0', '', ''), ('122', '114101040', '1', '4', '1700', 'Archon Brigade General\'s Shoes', 'Archon Brigade General\'s Shoes', '0', '', ''), ('123', '110301048', '1', '4', '1700', 'Archon Brigade General\'s Jerkin', 'Archon Brigade General\'s Jerkin', '0', '', ''), ('124', '111301004', '1', '4', '1700', 'Archon Brigade General\'s Vambrace', 'Archon Brigade General\'s Vambrace', '0', '', ''), ('125', '112300953', '1', '4', '1700', 'Archon Brigade General\'s Shoulderguards', 'Archon Brigade General\'s Shoulderguards', '0', '', ''), ('126', '113301024', '1', '4', '1700', 'Archon Brigade General\'s Breeches', 'Archon Brigade General\'s Breeches', '0', '', ''), ('127', '114301060', '1', '4', '1700', 'Archon Brigade General\'s Boots', 'Archon Brigade General\'s Boots', '0', '', ''), ('128', '110501016', '1', '4', '1700', 'Archon Brigade General\'s Hauberk', 'Archon Brigade General\'s Hauberk', '0', '', ''), ('129', '111500991', '1', '4', '1700', 'Archon Brigade General\'s Handguards', 'Archon Brigade General\'s Handguards', '0', '', ''), ('130', '112500940', '1', '4', '1700', 'Archon Brigade General\'s Spaulders', 'Archon Brigade General\'s Spaulders', '0', '', ''), ('131', '113500994', '1', '4', '1700', 'Archon Brigade General\'s Chausses', 'Archon Brigade General\'s Chausses', '0', '', ''), ('132', '114501003', '1', '4', '1700', 'Archon Brigade General\'s Brogans', 'Archon Brigade General\'s Brogans', '0', '', ''), ('133', '110600996', '1', '4', '1700', 'Archon Brigade General\'s Breastplate', 'Archon Brigade General\'s Breastplate', '0', '', ''), ('134', '111600982', '1', '4', '1700', 'Archon Brigade General\'s Gauntlets', 'Archon Brigade General\'s Gauntlets', '0', '', ''), ('135', '112600955', '1', '4', '1700', 'Archon Brigade General\'s Shoulderplates', 'Archon Brigade General\'s Shoulderplates', '0', '', ''), ('136', '113600965', '1', '4', '1700', 'Archon Brigade General\'s Greaves', 'Archon Brigade General\'s Greaves', '0', '', ''), ('137', '114600962', '1', '4', '1700', 'Archon Brigade General\'s Sabatons', 'Archon Brigade General\'s Sabatons', '0', '', ''), ('138', '169620000', '1', '3', '100', 'Lodas Amulet', 'Lodas Amulet', '0', '', ''), ('139', '125040115', '1', '18', '200', 'Springwater Spirit Hat', 'Springwater Spirit Hat', '0', '', ''), ('140', '125040116', '1', '18', '200', 'Ember Spirit Hat', 'Ember Spirit Hat', '0', '', ''), ('141', '125040118', '1', '18', '200', 'Agrint Hat', 'Agrint Hat', '0', '', ''), ('142', '125040120', '1', '18', '200', 'Moonflower Hat', 'Moonflower Hat', '0', '', ''), ('143', '125040117', '1', '18', '200', 'Wind Spirit Hat', 'Wind Spirit Hat', '0', '', ''), ('144', '125040119', '1', '18', '200', 'Kerubiel Hat', 'Kerubiel Hat', '0', '', ''), ('145', '100000879', '1', '18', '100', 'Aether Blade', 'Aether Blade', '0', '', ''), ('146', '100100613', '1', '18', '100', 'Sartuan\'s Snack', 'Sartuan\'s Snack', '0', '', ''), ('147', '100200803', '1', '18', '100', 'Mellin\'s Treat', 'Mellin\'s Treat', '0', '', ''), ('148', '100500695', '1', '18', '100', 'Siel\'s Timepiece', 'Siel\'s Timepiece', '0', '', ''), ('149', '100600752', '1', '18', '100', 'Rosetta\'s Tablet', 'Rosetta\'s Tablet', '0', '', ''), ('150', '100900681', '1', '18', '100', 'Cataclysm\'s Jaws', 'Cataclysm\'s Jaws', '0', '', ''), ('151', '101300813', '1', '18', '100', 'Seafoam\'s Glow', 'Seafoam\'s Glow', '0', '', ''), ('152', '101500695', '1', '18', '100', 'Israphel\'s Might', 'Israphel\'s Might', '0', '', ''), ('153', '101700719', '1', '18', '100', 'Artemana\'s Antlers', 'Artemana\'s Antlers', '0', '', ''), ('154', '115000959', '1', '18', '100', 'Aquilon\'s Crystal', 'Aquilon\'s Crystal', '0', '', ''), ('155', '125002355', '1', '13', '1300', 'Archon Brigade General\'s Chain Hood', 'Archon Brigade General\'s Chain Hood', '0', '', ''), ('156', '125002354', '1', '13', '1300', 'Archon Brigade General\'s Hat', 'Archon Brigade General\'s Hat', '0', '', ''), ('157', '125002353', '1', '13', '1300', 'Archon Brigade General\'s Headband', 'Archon Brigade General\'s Headband', '0', '', ''), ('158', '125002356', '1', '13', '1300', 'Archon Brigade General\'s Helm', 'Archon Brigade General\'s Helm', '0', '', '');
COMMIT;

-- ----------------------------
--  Table structure for `aionshop_transactions`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_transactions`;
CREATE TABLE `aionshop_transactions` (
  `transaction_id` bigint(20) NOT NULL auto_increment,
  `server_id` int(11) NOT NULL,
  `item_unique_id` int(11) NOT NULL,
  `buy_timestamp` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  `gift_receiver` varchar(50) default NULL,
  PRIMARY KEY  (`transaction_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `banned_ip`
-- ----------------------------
DROP TABLE IF EXISTS `banned_ip`;
CREATE TABLE `banned_ip` (
  `id` int(11) NOT NULL auto_increment,
  `mask` varchar(45) NOT NULL,
  `time_end` timestamp NULL default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `mask` (`mask`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `gameservers`
-- ----------------------------
DROP TABLE IF EXISTS `gameservers`;
CREATE TABLE `gameservers` (
  `id` int(11) NOT NULL auto_increment,
  `mask` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
