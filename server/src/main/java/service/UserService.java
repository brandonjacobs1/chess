package service;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import dataAccess.Interfaces.IUserDAO;
import dataAccess.MemoryAccess.MemoryAuthDAO;
import dataAccess.MemoryAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    IUserDAO userDAO = new MemoryUserDAO();
    IAuthDAO authDAO = new MemoryAuthDAO();
    public AuthData register(UserData user) throws DataAccessException {
        userDAO.createUser(user);
        return authDAO.createAuth(user);
    }
    public AuthData login(UserData user) throws DataAccessException {
        user = userDAO.getUser(user);
        return authDAO.createAuth(user);
    }
    public void logout(String authToken) throws DataAccessException {
     authDAO.deleteAuth(authToken);
    }

    public boolean authenticate(String token) {
        AuthData auth = authDAO.getAuth(token);
        return auth != null;
    }
}