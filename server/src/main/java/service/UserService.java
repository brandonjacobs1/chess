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
        return authDAO.upsertAuth(user);
    }
    public AuthData login(UserData user) throws DataAccessException {
        user = userDAO.getUser(user);
        return authDAO.upsertAuth(user);
    }
//    public void logout(UserData user) {}
}
