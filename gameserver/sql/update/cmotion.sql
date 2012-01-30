CREATE TABLE `player_cmotions` (
  `player_id` int(11) NOT NULL,
  `cmotion_id` int(11) NOT NULL,
  `cmotion_active` tinyint(1) NOT NULL DEFAULT '0',
  `cmotion_expires_time` bigint(20) NOT NULL DEFAULT '0',
  `cmotion_creation_time` timestamp NOT NULL DEFAULT '2011-01-01 00:00:01',
  PRIMARY KEY (`player_id`,`cmotion_id`),
  CONSTRAINT `player_cmotions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;