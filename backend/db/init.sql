CREATE DATABASE mainDB;
use mainDB;

CREATE TABLE users (
  userName VARCHAR(255),
  firstName VARCHAR(255),
  lastName VARCHAR(255),
  dailyScreenTime INT(255) UNSIGNED,
  dailyScore INT(255) UNSIGNED,
  totalScore INT(255) UNSIGNED,
  level INT(255) UNSIGNED,
  pfp VARCHAR(255),
  groupID1 VARCHAR(255),
  groupID2 VARCHAR(255),
  groupID3 VARCHAR(255)
);

CREATE TABLE groups (
  groupID INT(255) UNSIGNED,
  groupName VARCHAR(255),
  groupDesc VARCHAR(255)
);

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/Nocial-Data.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(userName,firstName,lastName,dailyScreenTime,dailyScore,totalScore,level,pfp,groupID1,groupID2,groupID3);


LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/GroupData.csv'
INTO TABLE groups
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(groupID,groupName,groupDesc)