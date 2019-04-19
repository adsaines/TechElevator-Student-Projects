BEGIN TRANSACTION;

--test information for golf courses
INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Avon Fields Golf Course', 'Cincinnati', 'Ohio', 45229, 66, '080000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Stonelick Hills Golf Course', 'Batavia', 'Ohio', 45103, 72, '064500', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Heatherwoode Golf Club', 'Springboro', 'Ohio', 45066, 71, '090000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('A. J. Jolly Golf Course', 'Alexandria', 'Kentucky', 41001, 71, '073000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Eagle Creek Country Club', 'Crittenden', 'Kentucky', 41030, 70, '080000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Hickory Sticks Golf Club', 'California', 'Kentucky', 41007, 71, '073000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('The Links at Rising Star Casino Resort', 'Rising Sun', 'Indiana', 47040, 72, '071500', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Grand Oak Golf Club', 'West Harrison', 'Indiana', 47060, 72, '070000', 'Dusk');

INSERT INTO courses (coursename, city, state, zip, par, courseopen, courseclose)
VALUES ('Harbor Links Golf Club', 'Brookville', 'Indiana', 47012, 72, '080000', 'Dusk');

--adding admins
--all passwords are "adminpass"
INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'observer',
    'vmpQ8wTK42uevC0w4gR8zMQw2Gi2DA/pe+Au1KDbmPDbM17CEZpetdWDXebHfGVWQWLiubFDeW9FxLKz+20FTAPCCaXuooBuYLJgiJYW5ODWGLnmL50DmKr4HcdeykSgiL0DxaTJefmV2sRfJwuplXhSyivUe1SrmbHWAoe2OzU=',
    'Administrator',
    'observer@observer.com', 
    'kzicFncOV7hFkQ7U886Z+A==', 
    'Alex', 
    'Saines');

INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'solog',
    'vmpQ8wTK42uevC0w4gR8zMQw2Gi2DA/pe+Au1KDbmPDbM17CEZpetdWDXebHfGVWQWLiubFDeW9FxLKz+20FTAPCCaXuooBuYLJgiJYW5ODWGLnmL50DmKr4HcdeykSgiL0DxaTJefmV2sRfJwuplXhSyivUe1SrmbHWAoe2OzU=',
    'Administrator',
    'solog@solog.com', 
    'kzicFncOV7hFkQ7U886Z+A==', 
    'Solomon', 
    'Gomez');

INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'pw',
    'vmpQ8wTK42uevC0w4gR8zMQw2Gi2DA/pe+Au1KDbmPDbM17CEZpetdWDXebHfGVWQWLiubFDeW9FxLKz+20FTAPCCaXuooBuYLJgiJYW5ODWGLnmL50DmKr4HcdeykSgiL0DxaTJefmV2sRfJwuplXhSyivUe1SrmbHWAoe2OzU=',
    'Administrator',
    'pw@pw.com', 
    'kzicFncOV7hFkQ7U886Z+A==', 
    'Paul', 
    'Leming');

INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'bkma',
    'vmpQ8wTK42uevC0w4gR8zMQw2Gi2DA/pe+Au1KDbmPDbM17CEZpetdWDXebHfGVWQWLiubFDeW9FxLKz+20FTAPCCaXuooBuYLJgiJYW5ODWGLnmL50DmKr4HcdeykSgiL0DxaTJefmV2sRfJwuplXhSyivUe1SrmbHWAoe2OzU=',
    'Administrator',
    'bkma@bkma.com', 
    'kzicFncOV7hFkQ7U886Z+A==', 
    'Brett', 
    'Anderson');
--populate with users
INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'TigerWoods',
    '1gxHX5RELq6VCHWpfZekK0gsgzOWoyCJOGQVUDnRd2WwER1fqrmhLcRTxcqzS5abBH/bXneaZ3WosVcbyVlXpiqZxWR7lfLm/jU5fuyfNfwc93H7yphTZYXBFzN9YOAPw+J2i/32By1bjp1Uoi/rmWtGAPmfa0INMe5LnSnKOJI=',
    'Golfer',
    'realTigerWoods@golf.com',
    'QToXFRH0oXmYyu1VMdUdfA==',
    'Tiger',
    'Woods');
INSERT INTO users (username, salt, userrole, email, userpassword, firstname, lastname)
VALUES (
    'Bartman',
    '7RF5k3JxyGBxQhte/RcsemHQW5MFeSk7uny0b76PcVPI/UWgRE1uCYdkzv56dqyBUXq6fCsOqt+dGhPfHcB8JE4/IrDeTXCdLJQ/LjwjhqxQKrNcTcqsZnBTKe32FmH7wYbqUxUq31tfhWP0n7I2lPFluKon/ILfBN4hXFe4RUQ=',
    'Golfer',
    'bart@simpsons.com',
    'cTCxz2lhwxKbAYZNp69uOA==',
    'Bart',
    'Simpson')''

--add a league per admin
INSERT INTO league (leagueName, courseId, courseName, leagueOrganizer) VALUES (
    'Bannana Daquiri',
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    'Avon Fields Golf Course',
    (SELECT userId FROM users WHERE username = 'observer'));
INSERT INTO league (leagueName, courseId, courseName, leagueOrganizer) VALUES (
    'Yingling',
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    'Stonelick Hills Golf Course',
    (SELECT userId FROM users WHERE username = 'solog'));
INSERT INTO league (leagueName, courseId, courseName, leagueOrganizer) VALUES (
    'Duff',
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    'Heatherwoode Golf Club',
    (SELECT userId FROM users WHERE username = 'pw'));
INSERT INTO league (leagueName, courseId, courseName, leagueOrganizer) VALUES (
    'Karuizawa',
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    'A. J. Jolly Golf Course',
    (SELECT userId FROM users WHERE username = 'bkma'));

--league members and scores
INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Bannana Daquiri'),
    (SELECT userId FROM users WHERE username = 'observer'),
    100
);
INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Bannana Daquiri'),
    (SELECT userId FROM users WHERE username = 'solog'),
    100
);

INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Yingling'),
    (SELECT userId FROM users WHERE username = 'solog'),
    100
);
INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Yingling'),
    (SELECT userId FROM users WHERE username = 'pw'),
    100
);

INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Duff'),
    (SELECT userId FROM users WHERE username = 'pw'),
    100
);
INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Duff'),
    (SELECT userId FROM users WHERE username = 'bkma'),
    100
);

INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Karuizawa'),
    (SELECT userId FROM users WHERE username = 'bkma'),
    100
);
INSERT INTO league_members_and_scores (leagueId, userId, bestScore) VALUES (
    (SELECT leagueId FROM league WHERE leagueName = 'Karuizawa'),
    (SELECT userId FROM users WHERE username = 'observer'),
    100
);

--users_courses
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    100
);

INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    100
);

INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    100
);

INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    100
);
INSERT INTO users_courses (userId, courseId, score) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    100
);

--tee time information

INSERT INTO tee_times (courseId, teeTime) VALUES (
    (SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'),
    '2019-05-10 10:00'
);
INSERT INTO tee_times (courseId, teeTime) VALUES (
    (SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'),
    '2019-05-10 10:00'
);
INSERT INTO tee_times (courseId, teeTime) VALUES (
    (SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'),
    '2019-05-10 10:00'
);
INSERT INTO tee_times (courseId, teeTime) VALUES (
    (SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'),
    '2019-05-10 10:00'
);

INSERT INTO user_teeTimes(userId, teeTimeId) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT teeTimeId FROM tee_times WHERE courseId = (
        SELECT courseId FROM courses WHERE courseName = 'Avon Fields Golf Course'
    ))
);
INSERT INTO user_teeTimes(userId, teeTimeId) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT teeTimeId FROM tee_times WHERE courseId = (
        SELECT courseId FROM courses WHERE courseName = 'Heatherwoode Golf Club'
    ))
);
INSERT INTO user_teeTimes(userId, teeTimeId) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT teeTimeId FROM tee_times WHERE courseId = (
        SELECT courseId FROM courses WHERE courseName = 'Stonelick Hills Golf Course'
    ))
);
INSERT INTO user_teeTimes(userId, teeTimeId) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT teeTimeId FROM tee_times WHERE courseId = (
        SELECT courseId FROM courses WHERE courseName = 'A. J. Jolly Golf Course'
    ))
);
--add invites for each admin
INSERT INTO league_invitation(organizerid, invitee, leagueid) VALUES (
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT leagueId FROM league WHERE leagueName = 'Bannana Daquiri')
);
INSERT INTO league_invitation(organizerid, invitee, leagueid) VALUES (
    (SELECT userId FROM users WHERE username = 'bkma'),
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT leagueId FROM league WHERE leagueName = 'Karuizawa')
);
INSERT INTO league_invitation(organizerid, invitee, leagueid) VALUES (
    (SELECT userId FROM users WHERE username = 'pw'),
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT leagueId FROM league WHERE leagueName = 'Duff')
);
INSERT INTO league_invitation(organizerid, invitee, leagueid) VALUES (
    (SELECT userId FROM users WHERE username = 'solog'),
    (SELECT userId FROM users WHERE username = 'observer'),
    (SELECT leagueId FROM league WHERE leagueName = 'Yingling')
);

COMMIT TRANSACTION;