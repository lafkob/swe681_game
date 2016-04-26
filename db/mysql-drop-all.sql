-- Useful for database changes where tables need to be recreated
SET FOREIGN_KEY_CHECKS=0;
drop table audit;
drop table games;
drop table user_roles;
drop table users;
SET FOREIGN_KEY_CHECKS=1;