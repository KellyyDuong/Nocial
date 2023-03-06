CREATE DATABASE mainDB;
use mainDB;

-- userName,firstName,lastName,dailyScreenTime,score,level
CREATE TABLE scores (
  userName VARCHAR(255) NOT NULL,
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255) NOT NULL,
  dailyScreenTime VARCHAR(255),
  score INT(255) UNSIGNED,
  level INT(255) UNSIGNED
);

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/Nocial-Data.csv'
INTO TABLE scores
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(userName,firstName,lastName,dailyScreenTime,score,level);
