-- ----------------------------
-- Table structure for `droplist`
-- ----------------------------
DROP TABLE IF EXISTS `droplist`;
CREATE TABLE `droplist` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobId` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL DEFAULT '0',
  `min` int(11) NOT NULL DEFAULT '0',
  `max` int(11) NOT NULL DEFAULT '0',
  `chance` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) DEFAULT CHARSET=utf8;

-- 216560
INSERT INTO droplist (mobId, itemId, min, max, chance) VALUES
(216560, 182400001, 741, 4660, 100.00),
(216560, 169000009, 1, 1, 40.00),
(216560, 182005545, 1, 1, 20.00),
(216560, 123000957, 1, 1, 20.00),
(216560, 141000001, 7, 1, 20.00),
(216560, 162000076, 1, 1, 20.00),
(216560, 162000092, 1, 1, 20.00),
(216560, 167000543, 1, 1, 20.00),
(216560, 188051115, 1, 1, 20.00);
-- 216559
INSERT INTO droplist (mobId, itemId, min, max, chance) VALUES
(216559, 182400001, 1218, 9287, 100.00),
(216559, 141000001, 5, 5, 41.18),
(216559, 169000009, 2, 2, 11.76),
(216559, 182005543, 2, 2, 11.76),
(216559, 152011065, 1, 1, 5.88),
(216559, 162000085, 1, 1, 5.88),
(216559, 162000086, 1, 1, 5.88),
(216559, 162000092, 1, 1, 5.88),
(216559, 167000543, 1, 1, 5.88),
(216559, 167000548, 1, 1, 5.88);
-- 214463
INSERT INTO droplist (mobId, itemId, min, max, chance) VALUES
(214463, 182207126, 445, 9413, 50),
(214464, 182207126, 530, 12780, 50),
(214465, 182207126, 788, 15223, 50),
(214466, 182207126, 792, 18307, 50);