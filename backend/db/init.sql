CREATE DATABASE mainDB;
use mainDB;

-- userName,firstName,lastName,dailyScreenTime,score,level
CREATE TABLE users (
  userName VARCHAR(255),
  firstName VARCHAR(255),
  lastName VARCHAR(255),
  dailyScreenTime VARCHAR(255),
  score INT(255) UNSIGNED,
  level INT(255) UNSIGNED,
  pfp VARCHAR(100) 
);

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/Nocial-Data.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(userName,firstName,lastName,dailyScreenTime,score,level,pfp);