CREATE TABLE `banned_mac` (
  `uniId` int(10) NOT NULL auto_increment,
  `address` varchar(20) NOT NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00' on update CURRENT_TIMESTAMP,
  `details` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`uniId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
