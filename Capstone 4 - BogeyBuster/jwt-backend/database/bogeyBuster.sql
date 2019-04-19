BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users_courses;

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
  CONSTRAINT chk_role CHECK (userRole IN ('Golfer', 'Administrator', 'League Administrator'))
);


CREATE TABLE courses (
  courseId serial,
  courseName varchar(255) NOT NULL,
  city varchar(32),
  state varchar(16),
  zip INT,
  par INT NOT NULL,
  courseOpen varchar(10),
  courseClose varchar(10),
  
  CONSTRAINT pk_courses PRIMARY KEY (courseId)
);

CREATE TABLE users_courses (
  userId INT,
  courseID INT NOT NULL,

  CONSTRAINT pk_user_courses PRIMARY KEY (userId, courseId),
  CONSTRAINT fk_user_courses_user FOREIGN KEY (userId) REFERENCES users (userId),
  CONSTRAINT fk_user_courses_courses FOREIGN KEY (courseId) REFERENCES courses (courseId)

);

COMMIT TRANSACTION;