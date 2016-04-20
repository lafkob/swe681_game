CREATE SCHEMA `traverse`;
  
-----------------------------------------------------
-- DDL for users table
-----------------------------------------------------
CREATE TABLE `traverse`.`users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(64) NOT NULL,
  `PASSWORD_HASH` VARCHAR(128) NOT NULL,
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
  KEY `FK_USERID_IDX` (`USERNAME`),
  CONSTRAINT `FK_USERID`
  	FOREIGN KEY (`USERNAME`)
  	REFERENCES users (`USERNAME`)
  	ON DELETE CASCADE);
  
--Try these:
-- INSERT INTO users (username, password_hash, enabled)
-- VALUES('user', '$2a$12$k4rfJbRtDYmGCHgSLMRVNOMn3UCq9ObAwuu4UIRH5VTo7IzY91iq.', true);
-- INSERT INTO user_roles (username, role)
-- VALUES('user', 'ROLE_USER');
-- The hash is of 'password'

-- INSERT INTO users (username, password_hash, enabled)
-- VALUES('klazo3', '$2a$12$IQ9ksZUwbeE.GQQOuuHsLOeSZhlWtciwj.5RO3t7vIhlxajkLGJr6', true);
-- INSERT INTO user_roles (username, role)
-- VALUES('klazo3', 'ROLE_USER');
-- The hash is of 'swe618'
  
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
-- DDL for statistics table
-----------------------------------------------------
CREATE TABLE `traverse`.`statistics` (
  `ID` INT NOT NULL,
  `WIN` INT NOT NULL,
  `LOSS` INT NOT NULL,
  `DRAW` INT NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USERID` (`ID`),
  CONSTRAINT `FK_USERID`
  	FOREIGN KEY (`ID`)
  	REFERENCES `traverse`.`users` (`ID`),
  	ON DELETE CASCADE);


