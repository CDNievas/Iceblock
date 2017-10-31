CREATE TABLE Person(
	id_person INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR(45),
	age INTEGER,
	weight FLOAT,
	id_address INTEGER,
	id_occupations INTEGER
);

CREATE TABLE Person_X_Occupation(
	id_person_occupation INTEGER IDENTITY PRIMARY KEY,
	id_person INTEGER,
	id_occupation INTEGER
);

CREATE TABLE Address(
	id_address INTEGER IDENTITY PRIMARY KEY,
	street VARCHAR(50),
	number INTEGER
);

CREATE TABLE Occupation(
	id_occupation INTEGER IDENTITY PRIMARY KEY,
	description VARCHAR(50),
	id_type_occupation INTEGER
);

CREATE TABLE Type_Occupation(
	id_type_occupation INTEGER IDENTITY PRIMARY KEY,
	description VARCHAR(50)
);

INSERT INTO Person VALUES (0,'Sarah',25,67.3,0,0);
INSERT INTO Person VALUES (1,'Tomy',5,20,0,null);
INSERT INTO Person VALUES (2,'George',30,80.1,0,1);
INSERT INTO Person VALUES (3,'Timmy',50,120.7,1,1);
INSERT INTO Person VALUES (4,'Monic',34,75.8,2,2);
INSERT INTO Person VALUES (5,'Alex',21,53.4,3,3);
INSERT INTO Person VALUES (6,'Tom',43,62.8,4,null);
INSERT INTO Person VALUES (7,'Asd',25,87.3,null,null);
INSERT INTO Person VALUES (8,'KK',10,0.9,null,null);
INSERT INTO Person VALUES (9,'Ernest',23,879.5,5,5);

INSERT INTO Person_X_Occupation VALUES (0,0,0);
INSERT INTO Person_X_Occupation VALUES (1,0,1);
INSERT INTO Person_X_Occupation VALUES (2,1,2);
INSERT INTO Person_X_Occupation VALUES (3,2,5);
INSERT INTO Person_X_Occupation VALUES (4,2,4);
INSERT INTO Person_X_Occupation VALUES (5,2,5);
INSERT INTO Person_X_Occupation VALUES (6,3,3);

INSERT INTO Address VALUES (0,'False',133);
INSERT INTO Address VALUES (1,'Collins',1241);
INSERT INTO Address VALUES (2,'5th Avenue',13);
INSERT INTO Address VALUES (3,'TheCakeIsALie',253);
INSERT INTO Address VALUES (4,'Lollipop',827);

INSERT INTO Occupation VALUES (0,'Chef',0);
INSERT INTO Occupation VALUES (1,'Mechanic',0);
INSERT INTO Occupation VALUES (2,'Lawyer',1);
INSERT INTO Occupation VALUES (3,'Enginner',1);
INSERT INTO Occupation VALUES (4,'Teacher',1);
INSERT INTO Occupation VALUES (5,'Firefighter',0);
INSERT INTO Occupation VALUES (6,'Thief',null);
INSERT INTO Occupation VALUES (7,'Drug Dealer',2);

INSERT INTO Type_Occupation VALUES (0,'Utility');
INSERT INTO Type_Occupation VALUES (1,'Professional');








