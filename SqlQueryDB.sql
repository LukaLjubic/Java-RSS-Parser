CREATE DATABASE LukaProjektJava;
go
USE LukaProjektJava;
go

CREATE TABLE PeoplesForaAuth (
	IDUser INT PRIMARY KEY IDENTITY,
	username NVARCHAR(300) UNIQUE,
	passwords NVARCHAR(300),
	administrator NVARCHAR(2)
);
GO
CREATE TABLE Movies (
	IDMovie INT PRIMARY KEY IDENTITY,
	MovieName NVARCHAR(300),
	Duration NVARCHAR(300),
	MovieDescription NVARCHAR(300),
	PhotoPath NVARCHAR(300)
);
GO
CREATE TABLE PeoplesForMovies (
	IDUserForMovies INT PRIMARY KEY IDENTITY,
	names NVARCHAR(300)
);
GO
CREATE TABLE MoviesAndActors (
	IDMoviesAndActors INT PRIMARY KEY IDENTITY,
	MoviesID int NOT NULL,
	UserID int NOT NULL,
	FOREIGN KEY (MoviesID) REFERENCES Movies(IDMovie),
	FOREIGN KEY (UserID) REFERENCES PeoplesForMovies(IDUserForMovies)
);
GO
CREATE TABLE MoviesAndDirectors (
	IDMoviesAndDirectors INT PRIMARY KEY IDENTITY,
	MoviesID int NOT NULL,
	UserID int NOT NULL,
	FOREIGN KEY (MoviesID) REFERENCES Movies(IDMovie),
	FOREIGN KEY (UserID) REFERENCES PeoplesForMovies(IDUserForMovies)
);
GO
------------------------------------------------------------------------------
INSERT INTO PeoplesForaAuth(username, passwords,administrator) VALUES('admin', 'admin', 'da')
GO
-------------------INSERT PROCEDURES-----------------------------------------
CREATE or ALTER PROCEDURE createPeople
	@names NVARCHAR(300),
	@IDUserForMovies INT OUTPUT
AS 
BEGIN 
	INSERT INTO PeoplesForMovies(names) VALUES(@names)
	SET @IDUserForMovies = SCOPE_IDENTITY()
END
GO 
CREATE or ALTER PROCEDURE createUser
	@username NVARCHAR(300),
	@passwords NVARCHAR(300),
	@IDUser INT OUTPUT
AS 
BEGIN 
	INSERT INTO PeoplesForaAuth(username, passwords,administrator) VALUES(@username, @passwords, 'ne')
	SET @IDUser = SCOPE_IDENTITY()
END
GO
CREATE or ALTER PROCEDURE createMovies
	@MovieName NVARCHAR(300),
	@MovieDuration NVARCHAr(300),
	@MovieDescription NVARCHAr(300),
	@PhotoPath NVARCHAR(300),
	@IDMovie INT OUTPUT
AS 
BEGIN 
	INSERT INTO Movies (MovieName, Duration, MovieDescription, PhotoPath) VALUES (@MovieName, @MovieDuration, @MovieDescription, @PhotoPath)
	SET @IDMovie = SCOPE_IDENTITY()
END
GO
CREATE or ALTER PROCEDURE createActorAndMovie
	@MovieID INT,
	@PeopleID NVARCHAr(300),
	@IDActorAndMovie INT OUTPUT
AS 
BEGIN 
	INSERT INTO MoviesAndActors (MoviesID, UserID) VALUES (@MovieID, @PeopleID)
	SET @IDActorAndMovie = SCOPE_IDENTITY()
END
GO
CREATE or ALTER PROCEDURE createDirectorAndMovie
	@MovieID INT,
	@PeopleID NVARCHAr(300),
	@IDDirectorAndMovie INT OUTPUT
AS 
BEGIN 
	INSERT INTO MoviesAndDirectors (MoviesID, UserID) VALUES (@MovieID, @PeopleID)
	SET @IDDirectorAndMovie = SCOPE_IDENTITY()
END
GO
-------------------------DELETE PROCEDURE---------------------
CREATE or ALTER PROCEDURE deleteUser
	@IDUser INT	 
AS 
BEGIN 
	DELETE  
	FROM 
			PeoplesForaAuth
	WHERE 
		IDUser = @IDUser
END
GO
-----------------------UPDATE PROCEDURE-------------------------
CREATE or ALTER PROCEDURE updateUser
	@IDUser int,
	@username NVARCHAR(300),
	@passwords NVARCHAR(300),
	@administrator NVARCHAR(2)
AS
BEGIN 
	UPDATE PeoplesForaAuth SET 
		username = @username,
		passwords = @passwords,
		administrator = @administrator
	WHERE 
		IDUser = @IDUser
END
go
CREATE or ALTER PROCEDURE updateMovie
	@IDMovie int,
	@MovieName NVARCHAR(300),
	@MovieDuration NVARCHAr(300),
	@MovieDescription NVARCHAr(300),
	@PhotoPath NVARCHAR(300)
AS
BEGIN 
	UPDATE Movies SET 
		MovieName = @MovieName,
		Duration = @MovieDuration,
		MovieDescription = @MovieDescription,
		PhotoPath = @PhotoPath 
	WHERE 
		IDMovie = @IDMovie
END
go
-----------------------------SELECT ONE PROCEDURE--------------------
CREATE or ALTER PROCEDURE authUser
	@username NVARCHAR(300),
	@passwords NVARCHAR(300)
AS
BEGIN 
	Select * from PeoplesForaAuth as pfa where pfa.username=@username and pfa.passwords=@passwords
END
GO

CREATE or ALTER PROCEDURE selectUser
	@IDUser INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		PeoplesForaAuth
	WHERE 
		IDUser = @IDUser
END
GO
CREATE or ALTER PROCEDURE selectMovie
	@IDMovie INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Movies
	WHERE 
		IDMovie = @IDMovie
END
GO
CREATE OR ALTER PROCEDURE selectMovieActors
	@IDMovie INT
AS 
BEGIN 
	SELECT 
		PF.IDUserForMovies as 'IDActorAndMovie',
		MA.UserID as 'IDUserForMovies',
		PF.names as 'names'
	FROM 
		MoviesAndActors AS MA
	INNER JOIN
		PeoplesForMovies AS PF ON MA.UserID = PF.IDUserForMovies
	WHERE 
		MA.MoviesID = @IDMovie
END
GO
CREATE OR ALTER PROCEDURE selectMovieDirectors
	@IDMovie INT
AS 
BEGIN 
	SELECT 
		PF.IDUserForMovies as 'IDDirectorAndMovie',
		MA.UserID as 'IDUserForMovies',
		PF.names as 'names'
	FROM 
		MoviesAndDirectors AS MA
	INNER JOIN
		PeoplesForMovies AS PF ON MA.UserID = PF.IDUserForMovies
	WHERE 
		MA.MoviesID = @IDMovie
END
GO
-----------------------------SELECT MANY PROCEDURE--------------------
CREATE or ALTER PROCEDURE selectPeople
AS 
BEGIN 
	SELECT * FROM PeoplesForMovies
END
GO
CREATE or ALTER PROCEDURE selectMovies
AS 
BEGIN 
	SELECT * FROM Movies
END
GO
-----------------------------DELETE PROCEDURE--------------------
CREATE or ALTER PROCEDURE deleteActors
	@IDMovie INT
AS 
BEGIN 
	DELETE FROM
		MoviesAndActors
	WHERE 
		MoviesID = @IDMovie
END
GO
CREATE or ALTER PROCEDURE deleteDirectors
	@IDMovie INT
AS 
BEGIN 
	DELETE FROM
		MoviesAndDirectors
	WHERE 
		MoviesID = @IDMovie
END
GO
CREATE or ALTER PROCEDURE deleteAll
AS 
BEGIN 
	DELETE FROM MoviesAndDirectors
	DELETE FROM MoviesAndActors
	DELETE FROM Movies
	DELETE FROM PeoplesForMovies
END
GO