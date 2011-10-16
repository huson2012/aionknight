-- ----------------------------
-- Table structure for `droplist`
-- ----------------------------
DROP TABLE IF EXISTS `droplist`;
CREATE TABLE `droplist` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobId` int(11) NOT NULL DEFAULT 0,
  `itemId` int(11) NOT NULL DEFAULT 0,
  `min` int(11) NOT NULL DEFAULT 0,
  `max` int(11) NOT NULL DEFAULT 0,
  `chance` float NOT NULL DEFAULT 0,
  PRIMARY KEY (`Id`)
) DEFAULT CHARSET=utf8;

-- 217734
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217734, 186000124, 1, 1, 27);
-- 217735
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217735, 186000124, 1, 1, 27);
-- 217736
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217736, 186000124, 1, 1, 27);
-- 217756
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217756, 186000124, 1, 1, 27);
-- 217757
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217757, 186000124, 1, 1, 27);
-- 217758
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217758, 186000124, 1, 1, 27);
-- 217759
INSERT INTO `droplist` (mobId, itemId, min, max, chance) VALUES
(217759, 182400001, 20000000, 20000000, 100),
(217759, 169500947, 1, 1, 2.3),
(217759, 169500951, 1, 1, 2.3),
(217759, 169500927, 1, 1, 2.3),
(217759, 169500931, 1, 1, 2.3),
(217759, 169500933, 1, 1, 2.3),
(217759, 169500935, 1, 1, 2.3);