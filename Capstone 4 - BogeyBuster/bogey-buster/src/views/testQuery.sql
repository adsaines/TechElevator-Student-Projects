SELECT * FROM courses WHERE state = 'IN';

SELECT league.leaguename, courses.coursename FROM league 
JOIN league_members_and_scores ON league.leagueid = league_members_and_scores.leagueid
JOIN courses ON league.courseid = courses.courseid WHERE league_members_and_scores.userid = 1;