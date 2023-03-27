CREATE DATABASE km;
USE km;
CREATE TABLE users (
	username VARCHAR(1),
	password VARCHAR(1),
	userid INT,
CONSTRAINT users_PK PRIMARY KEY (userid)
);

CREATE TABLE phonenumbers (
	phonenum VARCHAR(1),
	phoneid INT,
	userid INT,
CONSTRAINT phonenumbers_PK PRIMARY KEY (phoneid)
);


