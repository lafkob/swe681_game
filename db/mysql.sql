CREATE SCHEMA `traverse`;
  
-----------------------------------------------------
-- DDL for users table
-----------------------------------------------------
CREATE TABLE `traverse`.`users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(64) NOT NULL,
  `PASSWORD_HASH` VARCHAR(128) NOT NULL,
  `AUTH_ATTEMPTS` INT NOT NULL DEFAULT 0,
  `ENABLED` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `USERNAME_UNIQUE` (`USERNAME` ASC));
  

-----------------------------------------------------
-- DDL for user_roles table
-----------------------------------------------------
CREATE TABLE `traverse`.`user_roles` (
  `USER_ROLE_ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(64) NOT NULL,
  `ROLE` varchar(64) NOT NULL,
  PRIMARY KEY (`USER_ROLE_ID`),
  UNIQUE KEY `USERNAME_ROLE_UNIQUE` (`ROLE`,`USERNAME`),
  KEY `FK_USERNAME_IDX` (`USERNAME`),
  CONSTRAINT `FK_USERNAME`
  	FOREIGN KEY (`USERNAME`)
  	REFERENCES users (`USERNAME`)
  	ON DELETE CASCADE
  	ON UPDATE CASCADE);
  
-----------------------------------------------------
-- DDL for games table
-- -requires at least an id, a board, a status, and
--  one player
-----------------------------------------------------
CREATE TABLE `traverse`.`games` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `BOARD` LONGTEXT NULL,
  `STATUS` VARCHAR(64) NOT NULL,
  `PLAYER1_ID` INT NOT NULL,
  `PLAYER2_ID` INT NULL,
  `CURRENT_PLAYER_ID` INT NULL,
  `P1_ONE_MOVE_AGO_X` INT NULL,
  `P1_ONE_MOVE_AGO_Y` INT NULL,
  `P1_TWO_MOVE_AGO_X` INT NULL,
  `P1_TWO_MOVE_AGO_Y` INT NULL,
  `P1_ONE_ID_AGO` INT NULL,
  `P1_TWO_ID_AGO` INT NULL,
  `P2_ONE_MOVE_AGO_X` INT NULL,
  `P2_ONE_MOVE_AGO_Y` INT NULL,
  `P2_TWO_MOVE_AGO_X` INT NULL,
  `P2_TWO_MOVE_AGO_Y` INT NULL,
  `P2_ONE_ID_AGO` INT NULL,
  `P2_TWO_ID_AGO` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_PLAYER1_idx` (`PLAYER1_ID` ASC),
  INDEX `FK_PLAYER2_idx` (`PLAYER2_ID` ASC),
  INDEX `FK_CURRENT_PLAYER_idx` (`CURRENT_PLAYER_ID` ASC),
  CONSTRAINT `FK_PLAYER1`
    FOREIGN KEY (`PLAYER1_ID`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PLAYER2`
    FOREIGN KEY (`PLAYER2_ID`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CURRENT_PLAYER`
    FOREIGN KEY (`CURRENT_PLAYER_ID`)
    REFERENCES `traverse`.`users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-----------------------------------------------------
-- DDL for audit table
-----------------------------------------------------
CREATE TABLE `traverse`.`audit` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `GAME_ID` INT NOT NULL,
  `TIMESTAMP` TIMESTAMP NOT NULL,
  `PLAYER_ID` INT NOT NULL,
  `PIECE_ID` INT NULL,
  `MOVE` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `FK_GAME_ID_idx` (`GAME_ID` ASC),
  CONSTRAINT `FK_GAME_ID`
  	FOREIGN KEY (`GAME_ID`)
  	REFERENCES `traverse`.`games` (`ID`)
  	ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PLAYER_ID`
  	FOREIGN KEY (`PLAYER_ID`)
  	REFERENCES `traverse`.`users` (`ID`)
  	ON DELETE NO ACTION
    ON UPDATE NO ACTION);
