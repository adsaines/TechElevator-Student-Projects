BEGIN TRANSACTION;

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Avon Fields Golf Course', 'Cincinnati', 'Ohio', 45229, 66, '8:00am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Stonelick Hills Golf Course', 'Batavia', 'Ohio', 45103, 72, '6:45am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Heatherwoode Golf Club', 'Springboro', 'Ohio', 45066, 71, '9:00am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('A. J. Jolly Golf Course', 'Alexandria', 'Kentucky', 41001, 71, '7:30am', 'Dusk');
    
    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Eagle Creek Country Club', 'Crittenden', 'Kentucky', 41030, 70, '8:00am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Hickory Sticks Golf Club', 'California', 'Kentucky', 41007, 71, '7:30am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('The Links at Rising Star Casino Resort', 'Rising Sun', 'Indiana', 47040, 72, '7:15am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Grand Oak Golf Club', 'West Harrison', 'Indiana', 47060, 72, '7:00am', 'Dusk');

    INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
    VALUES ('Harbor Links Golf Club', 'Brookville', 'Indiana', 47012, 72, '8:00am', 'Dusk');


    INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
    VALUES ('sologomez', 'qwerty', 'Golfer', 'solo@gomez.com', 'sololinc', 'Solomon', 'Gomez');

    
COMMIT TRANSACTION;


SELECT * FROM courses

-idQuery-
SELECT courseId FROM courses WHERE coursename='Avon Fields Golf Course'
SELECT courseId FROM courses WHERE coursename='Eagle Creek Country Club'

-inputQuery-
INSERT INTO courses (courseName, par, zip, city, state, courseopen)
VALUES ('Warren', 66, 45555, 'Alex', 'Brett', '8:05am')

-findCourses-
SELECT * FROM courses WHERE courseName='Harbor Links Golf Club'
SELECT * FROM courses WHERE courseName='Grand Oak Golf Club'

-saveUser-
INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('ABSW', 'absw@absw.com', 'ABC123', 'qwerty2', 'Administrator', 'Best', 'Golfer') RETURNING userid
INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('2B2B', '2B2B@2B2B.com', 'ABC123', 'qwerty3', 'League Administrator', 'NotAGood', 'Golfer') RETURNING userid
INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('1A1A', '2B2B@2B2B.com', 'ABC123', 'qwerty3', 'League', 'NotAGood', 'Golfer') RETURNING userid

-changePassword-
UPDATE users SET userpassword='New123', salt='123qwerty' WHERE userid=4

-getRole-
SELECT userrole FROM users WHERE userid=1
SELECT userrole FROM users WHERE userid=4

-getPassword-
SELECT userpassword FROM users WHERE userid=3
SELECT userpassword FROM users WHERE userid=4

-loginUser-
SELECT * FROM users WHERE username = 'ABSW'

-getUserByUserName-
SELECT userid, username, userrole FROM users WHERE username = '2B2B'
SELECT userid, username, userrole FROM users WHERE username = 'ABSW'