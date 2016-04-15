CREATE SCHEMA `traverse`;
  

-----------------------------------------------------
-- DDL for users table
-----------------------------------------------------
CREATE TABLE `traverse`.`users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(64) NOT NULL,
  `PASSWORD_HASH` VARCHAR(128) NOT NULL,
  `PASSWORD_SALT` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `USERNAME_UNIQUE` (`USERNAME` ASC),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));
  

-----------------------------------------------------
-- DDL for user_roles table
-----------------------------------------------------
CREATE TABLE `traverse`.`user_roles` (
  `USER_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(64) NOT NULL,
  `ROLE` varchar(64) NOT NULL,
  PRIMARY KEY (`USER_ROLE_ID`),
  UNIQUE KEY `USERNAME_ROLE_UNIQUE` (`ROLE`,`USERNAME`),
  KEY `FK_USERID_IDX` (`USERNAME`),
  CONSTRAINT `FK_USERID` FOREIGN KEY (`USERNAME`) REFERENCES users (`USERNAME`));
  

-----------------------------------------------------
-- DDL for games table
-- -requires at least an id, a board, a status, and
--  one player
-----------------------------------------------------
CREATE TABLE `traverse`.`games` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `board` LONGTEXT NOT NULL,
  `status` VARCHAR(64) NOT NULL,
  `player1Id` INT NOT NULL,
  `player2Id` INT NULL,
  `currentPlayerId` INT NULL,
  `p1OneMoveAgoX` INT NULL,
  `p1OneMoveAgoY` INT NULL,
  `p1TwoMoveAgoX` INT NULL,
  `p1TwoMoveAgoY` INT NULL,
  `p1OneIdAgo` INT NULL,
  `p1TwoIdAgo` INT NULL,
  `p2OneMoveAgoX` INT NULL,
  `p2OneMoveAgoY` INT NULL,
  `p2TwoMoveAgoX` INT NULL,
  `p2TwoMoveAgoY` INT NULL,
  `p2OneIdAgo` INT NULL,
  `p2TwoIdAgo` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_PLAYER1_idx` (`player1Id` ASC),
  INDEX `FK_PLAYER2_idx` (`player2Id` ASC),
  INDEX `FK_CURRENT_PLAYER_idx` (`currentPlayerId` ASC),
  CONSTRAINT `FK_PLAYER1`
    FOREIGN KEY (`player1Id`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PLAYER2`
    FOREIGN KEY (`player2Id`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CURRENT_PLAYER`
    FOREIGN KEY (`currentPlayerId`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

