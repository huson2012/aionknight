CREATE TABLE IF NOT EXISTS `droplist` (
`Id` int(11) NOT NULL AUTO_INCREMENT,
`mobId` int(11) NOT NULL DEFAULT 0,
`itemId` int(11) NOT NULL DEFAULT 0,
`min` int(11) NOT NULL DEFAULT 0,
`max` int(11) NOT NULL DEFAULT 0,
`chance` FLOAT NOT NULL DEFAULT 0,
PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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