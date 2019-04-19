
SELECT * FROM courses;

--idQuery-
SELECT courseId FROM courses WHERE coursename='Avon Fields Golf Course';
SELECT courseId FROM courses WHERE coursename='Eagle Creek Country Club';

--inputQuery-
INSERT INTO courses (courseName, par, zip, city, state, courseopen)
VALUES ('Warren', 66, 45555, 'Alex', 'Brett', '134530');

--findCourses-
SELECT * FROM courses WHERE courseName='Harbor Links Golf Club';
SELECT * FROM courses WHERE courseName='Grand Oak Golf Club';

INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('ABSW', 'absw@absw.com', 'ABC123', 'qwerty2', 'Administrator', 'Best', 'Golfer');
INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('2B2B', '2B2B@2B2B.com', 'ABC123', 'qwerty3', 'League Administrator', 'NotAGood', 'Golfer');
INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES ('2B2B', '2B2B@2B2B.com', 'ABC123', 'qwerty3', 'League', 'NotAGood', 'Golfer');

--userid query
SELECT userid FROM users WHERE username = 'adsaines';

--search by sate
SELECT * FROM courses WHERE state LIKE '%Oh%';

--leagues
SELECT * FROM league;

--league scores with member names, id's, and best scores
SELECT league_members_and_scores.leagueid, league_members_and_scores.userid, users.username, bestscore FROM league_members_and_scores 
JOIN users ON league_members_and_scores.userid = users.userid
JOIN league ON league_members_and_scores.leagueid = league.leagueid
WHERE league_members_and_scores.leagueid = 1;

--finding a specific best score for a user
SELECT bestscore FROM league_members_and_scores 
WHERE leagueid = 1 AND userid = 2;

--get match info
SELECT users.username, tee_times.teetime FROM user_teetimes
JOIN users ON user_teetimes.userid = users.userid
JOIN tee_times ON user_teetimes.teetimeid = tee_times.teetimeid
WHERE tee_times.teetimeid = 2;

--accept invitation
INSERT INTO league_members_and_scores(leagueid,userid,bestscore) VALUES (2,1,0);

--count invitations
SELECT count(userid) FROM league_members_and_scores WHERE leagueid = 2 AND userid=1;

--delete invitations
DELETE FROM league_invitation WHERE organizerid=2 AND invitee=1 AND leagueid=2;

--get invitations with organizer name and league name
SELECT league_invitation.invitationid, league_invitation.organizerid, league_invitation.invitee, league_invitation.leagueid, league.leaguename, users.username
FROM league_invitation
JOIN users ON league_invitation.invitee = users.userid
JOIN league ON league_invitation.leagueid = league.leagueid 
WHERE league_invitation.invitee = 1;
