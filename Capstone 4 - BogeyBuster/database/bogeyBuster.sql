BEGIN TRANSACTION;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS users_courses CASCADE;
DROP TABLE IF EXISTS league CASCADE;
DROP TABLE IF EXISTS league_members_and_scores CASCADE;
DROP TABLE IF EXISTS league_invitation CASCADE;
DROP TABLE IF EXISTS tee_times CASCADE;
DROP TABLE IF EXISTS user_teeTimes CASCADE;

--each user a salt that is generated in java when their accounts are created
CREATE TABLE users (
  userId serial,
  username varchar(255) NOT NULL UNIQUE,
  salt varchar(256) NOT NULL,
  userRole varchar(255) NOT NULL DEFAULT ('Golfer'),
  email varchar(64) NOT NULL,
  userPassword varchar(32) NOT NULL,
  firstName varchar(32) NOT NULL,
  lastName varchar(32) NOT NULL,

  CONSTRAINT pk_users PRIMARY KEY (userId),
  CONSTRAINT chk_role CHECK (userRole IN ('Golfer', 'Administrator', 'League Organizer'))
);

--each course has a basic set of positional information
CREATE TABLE courses (
  courseId serial,
  courseName varchar(255) NOT NULL,
  city varchar(32),
  state varchar(16),
  zip INT,
  par INT NOT NULL,
  courseOpen TIME,
  courseClose varchar(10),
  
  CONSTRAINT pk_courses PRIMARY KEY (courseId)
);

--users can have multiple scores per course
CREATE TABLE users_courses (
  userId INT,
  courseId INT NOT NULL,
  score INT,

  CONSTRAINT fk_user_courses_user FOREIGN KEY (userId) REFERENCES users(userId),
  CONSTRAINT fk_user_courses_courses FOREIGN KEY (courseId) REFERENCES courses(courseId)
);

--leagues are organized by one user
CREATE TABLE league (
  leagueId serial,
  leagueName varchar(50) NOT NULL,
  courseId INT NOT NULL,
  courseName varchar(50) NOT NULL,
  leagueOrganizer INT NOT NULL,

  CONSTRAINT pk_id PRIMARY KEY (leagueId),
  CONSTRAINT fk_organizer_name FOREIGN KEY (leagueOrganizer) REFERENCES users(userId),
  CONSTRAINT fk_courseId FOREIGN KEY (courseId) REFERENCES courses(courseId)
);

--league members will display their best scores for that course, if no score is entered then they start with 1000
CREATE TABLE league_members_and_scores (
  leagueId INT NOT NULL,
  userId INT NOT NULL,
  bestScore INT DEFAULT (0),

  CONSTRAINT pk_name_course PRIMARY KEY (leagueId, userId),
  CONSTRAINT fk_userId FOREIGN KEY (userId) REFERENCES users(userId),
  CONSTRAINT fk_leagueId FOREIGN KEY (leagueId) REFERENCES league(leagueid)
);

CREATE TABLE league_invitation (
  organizerId INT NOT NULL,
  invitee INT NOT NULL,
  leagueId INT NOT NULL,
  invitationId serial,

  CONSTRAINT fk_organizerId FOREIGN KEY (organizerId) REFERENCES users(userId),
  CONSTRAINT fk_invitee FOREIGN KEY (invitee) REFERENCES users(userId),
  CONSTRAINT fk_leagueId FOREIGN KEY (leagueId) REFERENCES league(leagueId)
);

CREATE TABLE tee_times (
  teeTimeId serial,
  courseId INT NOT NULL,
  teeTime TIMESTAMP NOT NULL,

  CONSTRAINT pk_teeTimeId PRIMARY KEY (teeTimeId),
  CONSTRAINT fk_courseId_2 FOREIGN KEY (courseId) REFERENCES courses(courseId)
);

CREATE TABLE user_teeTimes(
  userId INT NOT NULL,
  teeTimeId INT NOT NULL,

  CONSTRAINT fk_userId_2 FOREIGN KEY (userId) REFERENCES users(userId),
  CONSTRAINT fk_teeTimeId FOREIGN KEY (teeTimeId) REFERENCES tee_times(teeTimeId)
);

COMMIT TRANSACTION;