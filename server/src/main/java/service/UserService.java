package service;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import dataAccess.Interfaces.IUserDAO;
import dataAccess.MemoryAccess.MemoryAuthDAO;
import dataAccess.MemoryAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import server.DuplicateEntryException;
import server.NotAuthenticatedException;

public class UserService {
    IUserDAO userDAO;
    IAuthDAO authDAO;

    public UserService() {
        authDAO = MemoryAuthDAO.getInstance();
        userDAO = MemoryUserDAO.getInstance();
    }
    public AuthData register(UserData user) throws DuplicateEntryException {
        try {
            userDAO.createUser(user);
            return authDAO.createAuth(user);
        } catch (DataAccessException e) {
            throw new DuplicateEntryException("User already exists");
        }
    }
    public AuthData login(UserData user) throws NotAuthenticatedException {
        try {
            user = userDAO.checkPassword(user);
            return authDAO.createAuth(user);
        }  catch (DataAccessException e) {
            throw new NotAuthenticatedException(e.getMessage());
        }
    }
    public void logout(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public boolean authenticate(String token) throws NotAuthenticatedException {
        try {
            AuthData auth = authDAO.getAuth(token);
            return auth != null;
        } catch (DataAccessException e) {
            throw new NotAuthenticatedException(e.getMessage());
        }
    }
    public UserData getUser(String token) throws NotAuthenticatedException {
        try {
            AuthData auth = authDAO.getAuth(token);
            return userDAO.getUser(auth.username());
        } catch (DataAccessException e) {
            throw new NotAuthenticatedException(e.getMessage());
        }
    }
    public void clear() {
        try {
            userDAO.clear();
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());        }
    }
}
