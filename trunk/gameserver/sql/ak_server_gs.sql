/*
 Source Server         : Aion-Knight RC1 [Beta]
 Source Server Type    : MySQL
 Source Server Version : 50045
 Source Host           : 192.168.0.1
 Source Database       : ak_server_gs

 Target Server Type    : MySQL
 Target Server Version : 50045
 File Encoding         : utf-8

 Date: 04/10/2011 08:33:19 PM
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `abyss_rank`
-- ----------------------------
DROP TABLE IF EXISTS `abyss_rank`;
CREATE TABLE `abyss_rank` (
  `player_id` int(11) NOT NULL,
  `daily_ap` int(11) NOT NULL,
  `weekly_ap` int(11) NOT NULL,
  `ap` int(11) NOT NULL,
  `rank` int(2) NOT NULL DEFAULT '1',
  `top_ranking` int(5) NOT NULL DEFAULT '0',
  `old_ranking` int(5) NOT NULL DEFAULT '0',
  `daily_kill` int(5) NOT NULL,
  `weekly_kill` int(5) NOT NULL,
  `all_kill` int(4) NOT NULL DEFAULT '0',
  `max_rank` int(2) NOT NULL DEFAULT '1',
  `last_kill` int(5) NOT NULL,
  `last_ap` int(11) NOT NULL,
  `last_update` decimal(20,0) NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abyss_rank
-- ----------------------------

-- ----------------------------
-- Table structure for `announcements`
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `announce` text NOT NULL,
  `faction` enum('ALL','ASMODIANS','ELYOS') NOT NULL DEFAULT 'ALL',
  `type` enum('ANNOUNCE','SHOUT','ORANGE','YELLOW','NORMAL') NOT NULL DEFAULT 'ANNOUNCE',
  `delay` int(4) NOT NULL DEFAULT '1800',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcements
-- ----------------------------

-- ----------------------------
-- Table structure for `blocks`
-- ----------------------------
DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks` (
  `player` int(11) NOT NULL,
  `blocked_player` int(11) NOT NULL,
  `reason` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`player`,`blocked_player`),
  KEY `blocked_player` (`blocked_player`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blocks
-- ----------------------------

-- ----------------------------
-- Table structure for `bookmark`
-- ----------------------------
DROP TABLE IF EXISTS `bookmark`;
CREATE TABLE `bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `char_id` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `world_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `char_id` (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bookmark
-- ----------------------------

-- ----------------------------
-- Table structure for `broker`
-- ----------------------------
DROP TABLE IF EXISTS `broker`;
CREATE TABLE `broker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `itemPointer` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL,
  `itemCount` bigint(20) NOT NULL,
  `seller` varchar(16) NOT NULL,
  `price` bigint(20) NOT NULL DEFAULT '0',
  `brokerRace` enum('ELYOS','ASMODIAN') NOT NULL,
  `expireTime` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `settleTime` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `sellerId` int(11) NOT NULL,
  `isSold` tinyint(1) NOT NULL,
  `isSettled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of broker
-- ----------------------------

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
  PRIMARY KEY (`Id`),
  UNIQUE KEY `mobId_itemId` (`mobId`,`itemId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of droplist
-- ----------------------------

-- ----------------------------
-- Table structure for `friends`
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `player` int(11) NOT NULL,
  `friend` int(11) NOT NULL,
  PRIMARY KEY (`player`,`friend`),
  KEY `friend` (`friend`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friends
-- ----------------------------

-- ----------------------------
-- Table structure for `guilds`
-- ----------------------------
DROP TABLE IF EXISTS `guilds`;
CREATE TABLE `guilds` (
  `player_id` int(11) NOT NULL,
  `guild_id` int(2) NOT NULL DEFAULT '0',
  `last_quest` int(6) NOT NULL DEFAULT '0',
  `complete_time` timestamp NULL DEFAULT NULL,
  `current_quest` int(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of guilds
-- ----------------------------

-- ----------------------------
-- Table structure for `inventory`
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `itemUniqueId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `itemCount` bigint(20) NOT NULL DEFAULT '0',
  `itemColor` int(11) NOT NULL DEFAULT '0',
  `itemOwner` int(11) NOT NULL,
  `itemCreator` varchar(50) NOT NULL,
  `itemCreationTime` timestamp NOT NULL DEFAULT '2010-01-01 00:00:01',
  `itemExistTime` bigint(20) NOT NULL DEFAULT '0',
  `itemTradeTime` int(11) NOT NULL DEFAULT '0',
  `isEquiped` tinyint(1) NOT NULL DEFAULT '0',
  `isSoulBound` tinyint(1) NOT NULL DEFAULT '0',
  `slot` int(11) NOT NULL DEFAULT '0',
  `itemLocation` tinyint(1) DEFAULT '0',
  `enchant` tinyint(1) DEFAULT '0',
  `itemSkin` int(11) NOT NULL DEFAULT '0',
  `fusionedItem` int(11) NOT NULL DEFAULT '0',
  `optionalSocket` int(1) NOT NULL DEFAULT '0',
  `optionalFusionSocket` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`itemUniqueId`),
  KEY `item_owner` (`itemOwner`),
  KEY `item_location` (`itemLocation`),
  KEY `is_equiped` (`isEquiped`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inventory
-- ----------------------------

-- ----------------------------
-- Table structure for `item_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `item_cooldowns`;
CREATE TABLE `item_cooldowns` (
  `player_id` int(11) NOT NULL,
  `delay_id` int(11) NOT NULL,
  `use_delay` smallint(5) unsigned NOT NULL,
  `reuse_time` bigint(13) NOT NULL,
  PRIMARY KEY (`player_id`,`delay_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `item_stones`
-- ----------------------------
DROP TABLE IF EXISTS `item_stones`;
CREATE TABLE `item_stones` (
  `itemUniqueId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `slot` int(2) NOT NULL,
  `category` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`itemUniqueId`,`slot`,`category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item_stones
-- ----------------------------

-- ----------------------------
-- Table structure for `legions`
-- ----------------------------
DROP TABLE IF EXISTS `legions`;
CREATE TABLE `legions` (
  `id` int(11) NOT NULL,
  `name` varchar(16) NOT NULL,
  `rank` int(11) NOT NULL DEFAULT '0',
  `oldrank` int(11) NOT NULL DEFAULT '0',
  `level` int(1) NOT NULL DEFAULT '1',
  `contribution_points` int(11) NOT NULL DEFAULT '0',
  `deputy_permission1` int(1) NOT NULL DEFAULT '0',
  `deputy_permission2` int(1) NOT NULL DEFAULT '0',
  `legionary_permission1` int(1) NOT NULL DEFAULT '0',
  `legionary_permission2` int(1) NOT NULL DEFAULT '0',
  `centurion_permission1` int(1) NOT NULL DEFAULT '0',
  `centurion_permission2` int(1) NOT NULL DEFAULT '0',
  `volunteer_permission1` int(1) NOT NULL DEFAULT '0',
  `volunteer_permission2` int(1) NOT NULL DEFAULT '0',
  `disband_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legions
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_announcement_list`
-- ----------------------------
DROP TABLE IF EXISTS `legion_announcement_list`;
CREATE TABLE `legion_announcement_list` (
  `legion_id` int(11) NOT NULL,
  `announcement` varchar(255) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `legion_id` (`legion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_announcement_list
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_emblems`
-- ----------------------------
DROP TABLE IF EXISTS `legion_emblems`;
CREATE TABLE `legion_emblems` (
  `legion_id` int(11) NOT NULL,
  `emblem_ver` int(3) NOT NULL DEFAULT '0',
  `color_r` int(3) NOT NULL DEFAULT '0',
  `color_g` int(3) NOT NULL DEFAULT '0',
  `color_b` int(3) NOT NULL DEFAULT '0',
  `custom` tinyint(1) NOT NULL DEFAULT '0',
  `emblem_data` longblob,
  PRIMARY KEY (`legion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_emblems
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_history`
-- ----------------------------
DROP TABLE IF EXISTS `legion_history`;
CREATE TABLE `legion_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `legion_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `history_type` enum('CREATE','JOIN','KICK','APPOINTED','EMBLEM_REGISTER','EMBLEM_MODIFIED') NOT NULL,
  `name` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `legion_id` (`legion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_history
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_members`
-- ----------------------------
DROP TABLE IF EXISTS `legion_members`;
CREATE TABLE `legion_members` (
  `legion_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `nickname` varchar(16) NOT NULL DEFAULT '',
  `rank` enum('BRIGADE_GENERAL','SUB_GENERAL','CENTURION','LEGIONARY','NEW_LEGIONARY') NOT NULL DEFAULT 'NEW_LEGIONARY',
  `selfintro` varchar(25) DEFAULT '',
  PRIMARY KEY (`player_id`),
  KEY `player_id` (`player_id`),
  KEY `legion_id` (`legion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_members
-- ----------------------------

-- ----------------------------
-- Table structure for `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `mailUniqueId` int(11) NOT NULL,
  `mailRecipientId` int(11) NOT NULL,
  `senderName` varchar(35) NOT NULL,
  `mailTitle` varchar(20) NOT NULL,
  `mailMessage` varchar(1000) NOT NULL,
  `unread` tinyint(4) NOT NULL DEFAULT '1',
  `attachedItemId` int(11) NOT NULL,
  `attachedKinahCount` bigint(20) NOT NULL,
  `express` tinyint(4) NOT NULL DEFAULT '0',
  `recievedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mailUniqueId`),
  KEY `mailRecipientId` (`mailRecipientId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------

-- ----------------------------
-- Table structure for `npc_shouts`
-- ----------------------------
DROP TABLE IF EXISTS `npc_shouts`;
CREATE TABLE `npc_shouts` (
  `message_id` int(11) NOT NULL,
  `npc_id` int(11) NOT NULL,
  `event` enum('NONE','ATK','CAST','DESPAWN','DIE','DIRECTION','FAIL','FLEE','HELP','IDLE','LEAVE','QUEST','RESETHATE','SEEUSER','SKILL','SLEEP','START','SWICHTARGET','WAKEUP','WAYPOINT','WIN','WOUNDED','YELL') NOT NULL DEFAULT 'NONE',
  `param` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`message_id`,`npc_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of npc_shouts
-- ----------------------------

-- ----------------------------
-- Table structure for `petitions`
-- ----------------------------
DROP TABLE IF EXISTS `petitions`;
CREATE TABLE `petitions` (
  `id` bigint(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `addData` varchar(255) DEFAULT NULL,
  `time` bigint(11) NOT NULL DEFAULT '0',
  `status` enum('PENDING','IN_PROGRESS','REPLIED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of petitions
-- ----------------------------

-- ----------------------------
-- Table structure for `players`
-- ----------------------------
DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_id` int(11) NOT NULL,
  `account_name` varchar(50) NOT NULL,
  `exp` bigint(20) NOT NULL DEFAULT '0',
  `recoverexp` bigint(20) NOT NULL DEFAULT '0',
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `gender` enum('MALE','FEMALE') NOT NULL,
  `race` enum('ASMODIANS','ELYOS') NOT NULL,
  `player_class` enum('WARRIOR','GLADIATOR','TEMPLAR','SCOUT','ASSASSIN','RANGER','MAGE','SORCERER','SPIRIT_MASTER','PRIEST','CLERIC','CHANTER') NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deletion_date` timestamp NULL DEFAULT NULL,
  `last_online` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `cube_size` tinyint(1) NOT NULL DEFAULT '0',
  `advanced_stigma_slot_size` tinyint(1) NOT NULL DEFAULT '0',
  `warehouse_size` tinyint(1) NOT NULL DEFAULT '0',
  `mailboxLetters` tinyint(4) NOT NULL DEFAULT '0',
  `bind_point` int(11) NOT NULL DEFAULT '0',
  `title_id` int(3) NOT NULL DEFAULT '-1',
  `online` tinyint(1) NOT NULL DEFAULT '0',
  `note` text,
  `repletionstate` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`),
  KEY `account_id` (`account_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of players
-- ----------------------------

-- ----------------------------
-- Table structure for `player_appearance`
-- ----------------------------
DROP TABLE IF EXISTS `player_appearance`;
CREATE TABLE `player_appearance` (
  `player_id` int(11) NOT NULL,
  `voice` int(11) NOT NULL,
  `skin_rgb` int(11) NOT NULL,
  `hair_rgb` int(11) NOT NULL,
  `lip_rgb` int(11) NOT NULL,
  `eye_rgb` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `hair` int(11) NOT NULL,
  `decoration` int(11) NOT NULL,
  `tattoo` int(11) NOT NULL,
  `face_contour` int(11) NOT NULL,
  `expression` int(11) NOT NULL,
  `jaw_line` int(11) NOT NULL,
  `forehead` int(11) NOT NULL,
  `eye_height` int(11) NOT NULL,
  `eye_space` int(11) NOT NULL,
  `eye_width` int(11) NOT NULL,
  `eye_size` int(11) NOT NULL,
  `eye_shape` int(11) NOT NULL,
  `eye_angle` int(11) NOT NULL,
  `brow_height` int(11) NOT NULL,
  `brow_angle` int(11) NOT NULL,
  `brow_shape` int(11) NOT NULL,
  `nose` int(11) NOT NULL,
  `nose_bridge` int(11) NOT NULL,
  `nose_width` int(11) NOT NULL,
  `nose_tip` int(11) NOT NULL,
  `cheeks` int(11) NOT NULL,
  `lip_height` int(11) NOT NULL,
  `mouth_size` int(11) NOT NULL,
  `lip_size` int(11) NOT NULL,
  `smile` int(11) NOT NULL,
  `lip_shape` int(11) NOT NULL,
  `chin_height` int(11) NOT NULL,
  `cheek_bones` int(11) NOT NULL,
  `ear_shape` int(11) NOT NULL,
  `head_size` int(11) NOT NULL,
  `neck` int(11) NOT NULL,
  `neck_length` int(11) NOT NULL,
  `shoulder_size` int(11) NOT NULL,
  `torso` int(11) NOT NULL,
  `chest` int(11) NOT NULL,
  `waist` int(11) NOT NULL,
  `hips` int(11) NOT NULL,
  `arm_thickness` int(11) NOT NULL,
  `hand_size` int(11) NOT NULL,
  `leg_thickness` int(11) NOT NULL,
  `foot_size` int(11) NOT NULL,
  `facial_ratio` int(11) NOT NULL,
  `face_shape` int(11) NOT NULL,
  `arm_length` int(11) NOT NULL,
  `leg_length` int(11) NOT NULL,
  `shoulders` int(11) NOT NULL,
  `height` float NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_appearance
-- ----------------------------

-- ----------------------------
-- Table structure for `player_effects`
-- ----------------------------
DROP TABLE IF EXISTS `player_effects`;
CREATE TABLE `player_effects` (
  `player_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `delay_id` int(11) NOT NULL DEFAULT '0',
  `skill_lvl` tinyint(4) NOT NULL,
  `current_time` int(11) NOT NULL,
  `reuse_delay` bigint(13) NOT NULL,
  PRIMARY KEY (`player_id`,`skill_id`,`delay_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_effects
-- ----------------------------

-- ----------------------------
-- Table structure for `player_emotions`
-- ----------------------------
DROP TABLE IF EXISTS `player_emotions`;
CREATE TABLE `player_emotions` (
  `player_id` int(11) NOT NULL,
  `emotion_id` int(11) NOT NULL,
  `emotion_expires_time` bigint(20) NOT NULL DEFAULT '0',
  `emotion_date` timestamp NOT NULL DEFAULT '2010-01-01 00:00:01',
  PRIMARY KEY (`player_id`,`emotion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_emotions
-- ----------------------------

-- ----------------------------
-- Table structure for player_cmotions
-- ----------------------------

CREATE TABLE `player_cmotions` (
  `player_id` int(11) NOT NULL,
  `cmotion_id` int(11) NOT NULL,
  `cmotion_active` tinyint(1) NOT NULL DEFAULT '0',
  `cmotion_expires_time` bigint(20) NOT NULL DEFAULT '0',
  `cmotion_creation_time` timestamp NOT NULL DEFAULT '2011-01-01 00:00:01',
  PRIMARY KEY (`player_id`,`cmotion_id`),
  CONSTRAINT `player_cmotions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_cmotions
-- ----------------------------

-- ----------------------------
-- Table structure for `player_instancecd`
-- ----------------------------
DROP TABLE IF EXISTS `player_instancecd`;
CREATE TABLE `player_instancecd` (
  `playerId` int(11) NOT NULL DEFAULT '0',
  `instanceMapId` int(11) NOT NULL DEFAULT '0',
  `CDEnd` timestamp NULL DEFAULT NULL,
  `instanceId` int(5) NOT NULL,
  `groupId` int(11) DEFAULT '0',
  PRIMARY KEY (`playerId`,`instanceMapId`,`instanceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_instancecd
-- ----------------------------

-- ----------------------------
-- Table structure for `player_life_stats`
-- ----------------------------
DROP TABLE IF EXISTS `player_life_stats`;
CREATE TABLE `player_life_stats` (
  `player_id` int(11) NOT NULL,
  `hp` int(11) NOT NULL DEFAULT '1',
  `mp` int(11) NOT NULL DEFAULT '1',
  `fp` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_life_stats
-- ----------------------------

-- ----------------------------
-- Table structure for `player_macrosses`
-- ----------------------------
DROP TABLE IF EXISTS `player_macrosses`;
CREATE TABLE `player_macrosses` (
  `player_id` int(11) NOT NULL,
  `order` int(3) NOT NULL,
  `macro` text NOT NULL,
  UNIQUE KEY `main` (`player_id`,`order`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_motion
-- ----------------------------

-- ----------------------------
-- Table structure for `player_passkey`
-- ----------------------------
DROP TABLE IF EXISTS `player_passkey`;
CREATE TABLE `player_passkey` (
  `account_id` int(11) NOT NULL,
  `passkey` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`account_id`,`passkey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_passkey
-- ----------------------------

-- ----------------------------
-- Table structure for `player_pets`
-- ----------------------------
DROP TABLE IF EXISTS `player_pets`;
CREATE TABLE `player_pets` (
  `player_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `decoration` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `feed_count` smallint(6) NOT NULL DEFAULT '0',
  `love_count` smallint(6) NOT NULL DEFAULT '0',
  `exp` smallint(6) NOT NULL DEFAULT '0',
  `feed_state` enum('HUNGRY','CONTENT','SEMIFULL','FULL') NOT NULL DEFAULT 'HUNGRY',
  `cd_started` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`pet_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_pets
-- ----------------------------

-- ----------------------------
-- Table structure for `player_punishments`
-- ----------------------------
DROP TABLE IF EXISTS `player_punishments`;
CREATE TABLE `player_punishments` (
  `player_id` int(11) NOT NULL,
  `punishment_status` tinyint(3) unsigned DEFAULT '0',
  `punishment_timer` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_punishments
-- ----------------------------

-- ----------------------------
-- Table structure for `player_quests`
-- ----------------------------
DROP TABLE IF EXISTS `player_quests`;
CREATE TABLE `player_quests` (
  `player_id` int(11) NOT NULL,
  `quest_id` int(10) unsigned NOT NULL DEFAULT '0',
  `status` varchar(10) NOT NULL DEFAULT 'NONE',
  `quest_vars` int(10) unsigned NOT NULL DEFAULT '0',
  `complete_count` int(3) unsigned NOT NULL DEFAULT '0',
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`,`quest_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_quests
-- ----------------------------

-- ----------------------------
-- Table structure for `player_recipes`
-- ----------------------------
DROP TABLE IF EXISTS `player_recipes`;
CREATE TABLE `player_recipes` (
  `player_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  PRIMARY KEY (`player_id`,`recipe_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_recipes
-- ----------------------------

-- ----------------------------
-- Table structure for `player_settings`
-- ----------------------------
DROP TABLE IF EXISTS `player_settings`;
CREATE TABLE `player_settings` (
  `player_id` int(11) NOT NULL,
  `settings_type` tinyint(1) NOT NULL,
  `settings` blob NOT NULL,
  PRIMARY KEY (`player_id`,`settings_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_settings
-- ----------------------------

-- ----------------------------
-- Table structure for `player_skills`
-- ----------------------------
DROP TABLE IF EXISTS `player_skills`;
CREATE TABLE `player_skills` (
  `player_id` int(11) NOT NULL,
  `skillId` int(11) NOT NULL,
  `skillLevel` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`,`skillId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_skills
-- ----------------------------

-- ----------------------------
-- Table structure for `player_titles`
-- ----------------------------
DROP TABLE IF EXISTS `player_titles`;
CREATE TABLE `player_titles` (
  `player_id` int(11) NOT NULL,
  `title_id` int(11) NOT NULL,
  `title_expires_time` bigint(20) NOT NULL DEFAULT '0',
  `title_date` timestamp NOT NULL DEFAULT '2010-01-01 00:00:01',
  PRIMARY KEY (`player_id`,`title_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_titles
-- ----------------------------

-- ----------------------------
-- Table structure for `player_world_bans`
-- ----------------------------
DROP TABLE IF EXISTS `player_world_bans`;
CREATE TABLE `player_world_bans` (
  `player` int(11) NOT NULL,
  `by` varchar(255) NOT NULL,
  `duration` bigint(11) NOT NULL,
  `date` bigint(11) NOT NULL,
  `reason` varchar(255) NOT NULL,
  PRIMARY KEY (`player`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_world_bans
-- ----------------------------

-- ----------------------------
-- Table structure for `purchase_limit`
-- ----------------------------
DROP TABLE IF EXISTS `purchase_limit`;
CREATE TABLE `purchase_limit` (
  `player_id` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `itemCount` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`itemId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of purchase_limit
-- ----------------------------

-- ----------------------------
-- Table structure for `server_variables`
-- ----------------------------
DROP TABLE IF EXISTS `server_variables`;
CREATE TABLE `server_variables` (
  `key` varchar(30) NOT NULL,
  `value` varchar(30) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of server_variables
-- ----------------------------

-- ----------------------------
-- Table structure for `siege_locations`
-- ----------------------------
DROP TABLE IF EXISTS `siege_locations`;
CREATE TABLE `siege_locations` (
  `id` int(11) NOT NULL,
  `race` enum('ELYOS','ASMODIANS','BALAUR') NOT NULL,
  `legion_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of siege_locations
-- ----------------------------

-- ----------------------------
-- Table structure for `siege_log`
-- ----------------------------
DROP TABLE IF EXISTS `siege_log`;
CREATE TABLE `siege_log` (
  `log_uuid` bigint(20) NOT NULL AUTO_INCREMENT,
  `legion_name` varchar(255) NOT NULL DEFAULT '',
  `action` enum('CAPTURE','DEFEND') NOT NULL,
  `tstamp` bigint(20) NOT NULL,
  `siegeloc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`log_uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of siege_log
-- ----------------------------

-- ----------------------------
-- Table structure for `spawns`
-- ----------------------------
DROP TABLE IF EXISTS `spawns`;
CREATE TABLE `spawns` (
  `spawn_id` int(11) NOT NULL AUTO_INCREMENT,
  `object_id` int(11) NOT NULL,
  `admin_id` int(11) NOT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `npc_id` int(11) NOT NULL,
  `respawn` tinyint(1) NOT NULL DEFAULT '0',
  `map_id` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `h` tinyint(8) NOT NULL,
  `spawned` tinyint(1) NOT NULL DEFAULT '0',
  `staticid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`spawn_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spawns
-- ----------------------------

-- ----------------------------
-- Table structure for `spawn_groups`
-- ----------------------------
DROP TABLE IF EXISTS `spawn_groups`;
CREATE TABLE `spawn_groups` (
  `admin_id` int(11) NOT NULL,
  `group_name` varchar(255) NOT NULL,
  `spawned` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`admin_id`,`group_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spawn_groups
-- ----------------------------

-- ----------------------------
-- Table structure for `special_spawns`
-- ----------------------------
DROP TABLE IF EXISTS `special_spawns`;
CREATE TABLE `special_spawns` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stage` int(11) NOT NULL,
  `round` int(11) NOT NULL,
  `npc_id` int(11) NOT NULL,
  `world_id` int(11) NOT NULL DEFAULT '0',
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `h` int(11) NOT NULL,
  `flag` int(11) NOT NULL,
  `race` enum('ALL','ASMODIANS','ELYOS') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of special_spawns
-- ----------------------------
INSERT INTO `special_spawns` VALUES ('1', '1', '1', '217489', '300300000', '349.404', '357.695', '96.09', '90', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('2', '1', '1', '217486', '300300000', '342.432', '356.481', '96.09', '90', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('3', '1', '2', '217492', '300300000', '347.74', '358.92', '96.09', '30', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('4', '1', '3', '217487', '300300000', '355.168', '349.212', '96.09', '10', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('5', '1', '4', '217491', '300300000', '339.32', '349.31', '96.09', '30', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('6', '1', '5', '217493', '300300000', '344.549', '351.234', '96.09', '30', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('7', '1', '1', '217480', '300300000', '349.404', '357.695', '96.09', '90', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('8', '1', '1', '217477', '300300000', '342.432', '356.481', '96.09', '90', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('9', '1', '2', '217483', '300300000', '347.74', '358.92', '96.09', '30', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('10', '1', '3', '217478', '300300000', '355.168', '349.212', '96.09', '10', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('11', '1', '4', '217482', '300300000', '339.32', '349.31', '96.09', '30', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('12', '1', '5', '217484', '300300000', '344.549', '351.234', '96.09', '30', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('13', '2', '0', '799568', '300300000', '344.549', '351.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('14', '2', '1', '217494', '300300000', '342.443', '357.509', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('15', '2', '1', '217495', '300300000', '345.083', '357.62', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('16', '2', '1', '217496', '300300000', '348.599', '356.796', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('17', '2', '2', '217497', '300300000', '351.77', '357.19', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('18', '2', '2', '217494', '300300000', '353.61', '354.65', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('19', '2', '2', '217496', '300300000', '345.522', '359.93', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('20', '2', '2', '217499', '300300000', '356.28', '348.88', '96.09', '90', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('21', '2', '3', '217500', '300300000', '350.365', '350.37', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('22', '2', '4', '217497', '300300000', '352.301', '347.989', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('23', '2', '4', '217499', '300300000', '346.549', '356.234', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('24', '2', '5', '217501', '300300000', '356.969', '348.935', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('25', '2', '6', '217737', '300300000', '345.549', '358.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('26', '3', '0', '799569', '300300000', '344.549', '350.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('27', '3', '1', '217511', '300300000', '329.88', '342.61', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('28', '3', '1', '217512', '300300000', '330.19', '346.75', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('29', '3', '1', '217513', '300300000', '330.44', '349.41', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('30', '3', '1', '217514', '300300000', '330.39', '351.87', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('31', '3', '2', '217515', '300300000', '331.51', '335.25', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('32', '3', '2', '217516', '300300000', '328.74', '343.9', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('33', '3', '2', '217517', '300300000', '331.53', '353.5', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('34', '3', '2', '217518', '300300000', '336.77', '359.82', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('35', '3', '3', '217519', '300300000', '342.16', '330.021', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('36', '3', '3', '217520', '300300000', '334.37', '341.46', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('37', '3', '3', '217521', '300300000', '336.73', '352.51', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('38', '3', '3', '217522', '300300000', '343.97', '361.11', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('39', '3', '4', '217524', '300300000', '337.94', '368.53', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('40', '3', '4', '217526', '300300000', '365.3', '358.56', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('41', '3', '4', '217523', '300300000', '333.26', '340.94', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('42', '3', '4', '217525', '300300000', '355.49', '333.78', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('43', '3', '5', '217528', '300300000', '356.28', '347', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('44', '3', '5', '217527', '300300000', '354.6', '339.76', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('45', '3', '6', '217740', '300300000', '347.549', '1783', '96.09', '0', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('46', '3', '6', '217741', '300300000', '347.549', '1783', '96.09', '0', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('47', '3', '6', '217742', '300300000', '347.549', '1783', '96.09', '0', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('48', '3', '6', '217743', '300300000', '347.549', '1784', '96.09', '0', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('49', '4', '0', '205331', '300300000', '347.549', '352.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('50', '4', '1', '217557', '300300000', '331.92', '349.18', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('51', '4', '1', '217559', '300300000', '332.77', '345.44', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('52', '4', '1', '217558', '300300000', '333.67', '353.91', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('53', '4', '2', '217560', '300300000', '338.02', '354.88', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('54', '4', '2', '217559', '300300000', '338.11', '351.68', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('55', '4', '2', '217562', '300300000', '338.32', '348.51', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('56', '4', '3', '217564', '300300000', '333.67', '350.31', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('57', '4', '3', '217563', '300300000', '337.35', '340', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('58', '4', '3', '217565', '300300000', '336.8', '357.68', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('59', '4', '4', '217567', '300300000', '347.549', '352.234', '96.09', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('60', '4', '5', '217565', '300300000', '340.34', '350.14', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('61', '4', '5', '217563', '300300000', '346.48', '344.78', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('62', '4', '5', '217564', '300300000', '354.66', '351.46', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('63', '4', '5', '217566', '300300000', '346.35', '352.234', '96.09', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('64', '4', '6', '217750', '300300000', '347.549', '352.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('65', '4', '6', '217740', '300300000', '347.549', '352.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('66', '4', '6', '217741', '300300000', '347.549', '352.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('67', '4', '6', '217742', '300300000', '347.549', '352.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('68', '4', '6', '217743', '300300000', '347.549', '352.234', '96.09', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('69', '5', '0', '205332', '300300000', '1260.13', '801.947', '358.605', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('70', '5', '1', '217549', '300300000', '1265.56', '796.22', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('71', '5', '1', '217548', '300300000', '1266.82', '799.29', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('72', '5', '1', '217547', '300300000', '1264.96', '804.46', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('73', '5', '1', '217545', '300300000', '1259.84', '795.06', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('74', '5', '1', '217550', '300300000', '1264.08', '808.03', '360.089', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('75', '5', '2', '217552', '300300000', '1261.84', '801.947', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('76', '5', '3', '217553', '300300000', '1264.58', '789.69', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('77', '5', '4', '217554', '300300000', '1263.84', '803.947', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('78', '5', '5', '217556', '300300000', '1265.08', '790.13', '358.605', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('79', '6', '0', '205333', '300300000', '1625.08', '159.152', '127.459', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('80', '6', '1', '217568', '300300000', '1625', '161.5', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('81', '6', '1', '217568', '300300000', '1622', '153', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('82', '6', '1', '217568', '300300000', '1638', '152.87', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('83', '6', '1', '217568', '300300000', '1641', '135', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('84', '6', '2', '217569', '300300000', '1638', '134', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('85', '6', '2', '217570', '300300000', '1639', '135', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('86', '6', '2', '211984', '300300000', '1640', '134', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('87', '6', '3', '217569', '300300000', '1638', '134', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('88', '6', '3', '217572', '300300000', '1632.21', '138.84', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('89', '6', '3', '217568', '300300000', '1640', '134', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('90', '6', '3', '217569', '300300000', '1641', '135', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('91', '6', '3', '217568', '300300000', '1642', '136', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('92', '6', '4', '217573', '300300000', '1627.15', '151.87', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('93', '6', '5', '217754', '300300000', '1636.43', '153.85', '126', '60', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('94', '6', '6', '217750', '300300000', '1638', '134', '126', '60', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('95', '7', '0', '205334', '300300000', '1781.61', '796.922', '470.734', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('96', '7', '1', '217582', '300300000', '1794.91', '811.994', '469.35', '80', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('97', '7', '2', '217583', '300300000', '1799.78', '790.6', '469.35', '80', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('98', '7', '3', '217584', '300300000', '1775.33', '805.61', '469.35', '80', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('99', '7', '4', '217585', '300300000', '1784.79', '787.91', '469.35', '80', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('100', '7', '5', '217587', '300300000', '1786.12', '797.88', '469.35', '80', '0', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('101', '7', '1', '217578', '300300000', '1794.91', '811.994', '469.35', '80', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('102', '7', '2', '217579', '300300000', '1799.78', '790.6', '469.35', '80', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('103', '7', '3', '217580', '300300000', '1775.33', '805.61', '469.35', '80', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('104', '7', '4', '217581', '300300000', '1784.79', '787.91', '469.35', '80', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('105', '7', '5', '217586', '300300000', '1786.12', '797.88', '469.35', '80', '0', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('106', '8', '0', '205335', '300300000', '1776.76', '1764.71', '303.695', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('107', '8', '1', '217590', '300300000', '1775.92', '1760.3', '303.695', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('108', '8', '2', '217592', '300300000', '1775.92', '1760.3', '303.695', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('109', '8', '3', '217591', '300300000', '1775.92', '1760.3', '303.695', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('110', '8', '4', '217589', '300300000', '1775.92', '1760.3', '303.695', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('111', '8', '5', '217593', '300300000', '1775.92', '1760.3', '303.695', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('112', '9', '0', '205336', '300300000', '1309.31', '1732.54', '316.095', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('113', '9', '1', '217594', '300300000', '1303.66', '1732.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('114', '9', '2', '217595', '300300000', '1303.66', '1732.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('115', '9', '3', '217596', '300300000', '1303.66', '1732.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('116', '9', '3', '217597', '300300000', '1323.66', '1750.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('117', '9', '4', '217598', '300300000', '1303.66', '1732.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('118', '9', '5', '217599', '300300000', '1303.66', '1732.81', '316.095', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('119', '9', '6', '217754', '300300000', '1303.66', '1732.81', '316.095', '0', '2', 'ALL');
INSERT INTO `special_spawns` VALUES ('120', '10', '0', '205337', '300300000', '1766.16', '1291.98', '395.797', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('121', '10', '1', '217600', '300300000', '1776.48', '1277.52', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('122', '10', '1', '217602', '300300000', '1781.36', '1288.67', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('123', '10', '1', '217601', '300300000', '1778.58', '1298.85', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('124', '10', '1', '217600', '300300000', '1768.28', '1305.82', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('125', '10', '1', '217602', '300300000', '1759.11', '1298.43', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('126', '10', '1', '217600', '300300000', '1755.32', '1289.43', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('127', '10', '1', '217601', '300300000', '1754.48', '1278.1', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('128', '10', '1', '217600', '300300000', '1768', '1296.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('129', '10', '1', '217601', '300300000', '1774.11', '1297.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('130', '10', '1', '217600', '300300000', '1775.11', '1298.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('131', '10', '2', '217603', '300300000', '1759.05', '1275.95', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('132', '10', '2', '217604', '300300000', '1772.22', '1278.74', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('133', '10', '2', '217605', '300300000', '1779.42', '1295.14', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('134', '10', '2', '217606', '300300000', '1772', '1303.88', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('135', '10', '2', '217603', '300300000', '1756.73', '1297.59', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('136', '10', '3', '217607', '300300000', '1766.11', '1289.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('137', '10', '3', '700439', '300300000', '1781.69', '1290.82', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('138', '10', '4', '217608', '300300000', '1766.11', '1289.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('143', '10', '4', '217604', '300300000', '1765.55', '1281.54', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('144', '10', '4', '217604', '300300000', '1771.79', '1279.17', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('145', '10', '4', '217604', '300300000', '1765.53', '1296.9', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('146', '10', '4', '217604', '300300000', '1754', '1285.77', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('147', '10', '5', '217609', '300300000', '1766.11', '1289.07', '394.237', '0', '0', 'ALL');
INSERT INTO `special_spawns` VALUES ('148', '1', '10', '799570', '300300000', '345.16', '348.723', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('149', '2', '10', '799571', '300300000', '344.549', '351.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('150', '3', '10', '799572', '300300000', '344.549', '351.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('151', '4', '10', '205338', '300300000', '344.549', '351.234', '96.09', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('152', '5', '10', '205339', '300300000', '1260.15', '795.075', '358.605', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('153', '6', '10', '205340', '300300000', '1625.08', '159.15', '126', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('154', '7', '10', '205341', '300300000', '1781.61', '796.92', '469.8', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('155', '8', '10', '205342', '300300000', '1776.76', '1764.62', '303.695', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('156', '9', '10', '205343', '300300000', '1309.64', '1732.64', '316.095', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('157', '10', '10', '205344', '300300000', '1766.16', '1291.98', '395.797', '0', '1', 'ALL');
INSERT INTO `special_spawns` VALUES ('158', '1', '10', '217734', '300300000', '345', '355', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('159', '1', '10', '217734', '300300000', '346', '350', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('160', '1', '10', '217734', '300300000', '345', '351', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('161', '1', '10', '217734', '300300000', '346', '352', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('162', '1', '10', '217734', '300300000', '345', '353', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('163', '1', '10', '217734', '300300000', '346', '354', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('164', '1', '10', '217756', '300300000', '345', '355', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('165', '1', '10', '217756', '300300000', '346', '350', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('166', '1', '10', '217756', '300300000', '345', '351', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('167', '1', '10', '217756', '300300000', '346', '352', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('168', '1', '10', '217756', '300300000', '345', '353', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('169', '1', '10', '217756', '300300000', '346', '354', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('170', '3', '10', '217735', '300300000', '345', '349', '96', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('171', '3', '10', '217757', '300300000', '346', '349', '96', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('182', '6', '10', '217736', '300300000', '1638', '134', '126', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('183', '6', '10', '217736', '300300000', '1639', '136', '126', '0', '1', 'ELYOS');
INSERT INTO `special_spawns` VALUES ('184', '6', '10', '217758', '300300000', '1638', '134', '126', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('185', '6', '10', '217758', '300300000', '1639', '136', '126', '0', '1', 'ASMODIANS');
INSERT INTO `special_spawns` VALUES ('186', '7', '10', '217759', '300300000', '1796', '815', '469', '0', '1', 'ALL');

-- ----------------------------
-- Table structure for `surveys`
-- ----------------------------
DROP TABLE IF EXISTS `surveys`;
CREATE TABLE `surveys` (
  `survey_id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `title` varchar(80) NOT NULL,
  `message` varchar(1024) NOT NULL,
  `select_text` varchar(50) NOT NULL,
  `itemId` int(11) NOT NULL DEFAULT '0',
  `itemCount` bigint(20) NOT NULL DEFAULT '0',
  `itemTradeTime` int(11) NOT NULL DEFAULT '0',
  `itemExistTime` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`survey_id`),
  KEY `player_id` (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of surveys
-- ----------------------------