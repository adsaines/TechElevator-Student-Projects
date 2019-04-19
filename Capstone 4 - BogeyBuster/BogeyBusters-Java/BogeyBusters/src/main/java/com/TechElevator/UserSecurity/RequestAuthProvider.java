package com.TechElevator.UserSecurity;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.TechElevator.DAO.UserDao;
import com.TechElevator.Pojos.User;

/**
 * RequestAuthProvider
 */
@Component
public class RequestAuthProvider implements AuthProvider {

    private HttpServletRequest request;
    private UserDao dao;
    public final static String USER_KEY = "appCurrentUser";
    public final static String ADMIN = "Administrator";
	public final static String LEAGUE_ORGANIZER = "League Organizer";
	public final static String USER = "Golfer";

    @Autowired
    public RequestAuthProvider(HttpServletRequest request, UserDao dao) {
        this.request = request;
        this.dao = dao;
    }

    @Override
    public boolean isLoggedIn() {
        return (request.getAttribute(USER_KEY) != null);
    }

    @Override
    public User getCurrentUser() {
        return (User) request.getAttribute(USER_KEY);
    }

    @Override
    public boolean signIn(String username, String password) {
        User authenticatedUser = dao.getValidUserWithPassword(username, password);
        if(authenticatedUser != null) {
            request.setAttribute(USER_KEY, authenticatedUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logOff() {
        request.removeAttribute(USER_KEY);
    }

    @Override
    public boolean changePassword(String existingPassword, String newPassword) {
        User userFromSession = (User) request.getAttribute(USER_KEY);
        if(userFromSession == null) {
            return false;
        }
        User userFromDb = dao.getValidUserWithPassword(userFromSession.getUsername(), existingPassword);
        if(userFromDb != null && userFromDb.getUserId() == userFromDb.getUserId()) {
            dao.changePassword(userFromSession, newPassword);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void register(User user) {
        dao.saveUser(user);
    }

    @Override
    public boolean userHasRole(String[] roles) {
        User currentUser = getCurrentUser();
        if(currentUser != null && roles != null) {
            return Arrays.asList(roles).contains(currentUser.getRole());
        } else {
            return false;
        }
    }
}