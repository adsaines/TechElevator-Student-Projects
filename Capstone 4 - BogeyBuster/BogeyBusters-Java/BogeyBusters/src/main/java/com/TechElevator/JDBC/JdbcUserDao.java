package com.TechElevator.JDBC;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.TechElevator.DAO.UserDao;
import com.TechElevator.Pojos.AbbreviatedUser;
import com.TechElevator.Pojos.Course;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.User;
import com.TechElevator.UserSecurity.PasswordHasher;

@Component
public class JdbcUserDao implements UserDao {
	
	private JdbcTemplate jdbcTemplate;
	private PasswordHasher passwordHasher;

	@Autowired
	public JdbcUserDao(DataSource datasource, PasswordHasher passwordHasher) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
		this.passwordHasher = passwordHasher;
	}
	
	@Override
	public User saveUser(User user) {
		// hash the password
		byte[] salt = passwordHasher.generateRandomSalt();
		String hashedPassword = passwordHasher.computeHash(user.getPassword(), salt);
		String saltString = new String(Base64.encode(salt));
		
		// define queries
		String insertQuery = "INSERT INTO users(username, email, userpassword, salt, userrole, firstname, lastname) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String idQuery = "SELECT userid FROM users WHERE username = ?";
		
		// run the queries
		jdbcTemplate.update(insertQuery, user.getUsername(), user.getEmail(), hashedPassword, saltString, user.getRole(), user.getFirstName(), user.getLastName() );
		
        return user;
	}

	@Override
	public void changePassword(User user, String newPassword) {
		byte[] salt = passwordHasher.generateRandomSalt();
        String hashedPassword = passwordHasher.computeHash(newPassword, salt);
        String saltString = new String(Base64.encode(salt));
        
        jdbcTemplate.update("UPDATE users SET userpassword=?, salt=? WHERE userid=?", hashedPassword, saltString, user.getUserId());
	}

	@Override
	public String getRole(long id) {
		String sqlGetRole = "SELECT userrole FROM users WHERE userid=?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetRole, id);
		String role = result.getString(0);
		return role;
	}

	@Override
	public String getPassword(long id) {
		String sqlGetPassword = "SELECT userpassword FROM users WHERE userid=?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetPassword, id);
		String password = result.getString(0);
		return password;
	}

	@Override
	public boolean loginUser(String username, String password) {
		String sqlValidUser = "SELECT * FROM users WHERE username = ?";
	     SqlRowSet results = jdbcTemplate.queryForRowSet(sqlValidUser, username);
	        if (results.next()) {
	            String storedSalt = results.getString("salt");
	            String storedPassword = results.getString("userpassword");
	            String hashedPassword = passwordHasher.computeHash(password, Base64.decode(storedSalt));
	            if(storedPassword.equals(hashedPassword)) {
	                return true;
	            } else {
	                return false;
	            }
	        } else {
	            return false;
	        }
	}
	
	public User mapResultToUser(SqlRowSet results) {
        User user = new User();
        user.setUserId(results.getLong("userid"));
        user.setUsername(results.getString("username"));
        user.setRole(results.getString("userrole"));
        user.setEmail(results.getString("email"));
        user.setFirstName(results.getString("firstName"));
        user.setLastName(results.getString("lastName"));
        user.setPassword(results.getString("userpassword"));
        return user;
    }

	@Override
	public User getUserByUsername(String username) {
		String sqlSelectUserByUsername = "SELECT * FROM users WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserByUsername, username);

        if(results.next()) {
        	
            return mapResultToUser(results);
        } else {
            return null;
        }
	}
	
	@Override
	public boolean checkForUsername(String username) {
		String sqlSelectUserByUsername = "SELECT * FROM users WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserByUsername, username);

        if(results.next()) {
        	return true;
        } else {
            return false;
        }
	}

	@Override
	public User getValidUserWithPassword(String username, String password) {
		 String sqlSearchForUser = "SELECT * FROM users WHERE username = ?";

	        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUser, username);
	        if (results.next()) {
	        	
	        	String storedSalt = results.getString("salt");
	            String storedPassword = results.getString("userpassword");
	            String hashedPassword = passwordHasher.computeHash(password, Base64.decode(storedSalt));
	            if(storedPassword.equals(hashedPassword)) {
	                return mapResultToUser(results);
	            } else {
	                return null;
	            }
	        } else {
	            return null;
	        }
	    }

	@Override
	public List<AbbreviatedUser> getUsersByStringSearch(String search) {
		List<AbbreviatedUser> similarNamedUsers = new ArrayList<AbbreviatedUser>();
		Map<Long, User> userMap = new LinkedHashMap<Long, User>();
		String userQuery = "SELECT * FROM users WHERE username LIKE ?;";
		
		// get the users by username
		SqlRowSet users = jdbcTemplate.queryForRowSet(userQuery, "%" + search + "%");
		
		// populate list
		while (users.next()) {
			AbbreviatedUser tom = new AbbreviatedUser();
			tom.setUserId(users.getLong("userid"));
			tom.setUsername(users.getString("username"));
			
			similarNamedUsers.add(tom);
		}
		
		return similarNamedUsers;
	}

	@Override
	public void addScoreForUser(User user, Course course, int score) {
		String sqlAddScore = "INSERT INTO users_courses (userid, courseid, score) VALUES (?, ?, ?)";
		jdbcTemplate.update(sqlAddScore, user.getUserId(), course.getId(), score);
		
	}

	@Override
	public boolean isLeagueOrganizer(User user, League league) {
		
		if (user.getUserId() == league.getOrganizerId()) {
			return true;
		}
		else {
			return false;
		}
	}
}
