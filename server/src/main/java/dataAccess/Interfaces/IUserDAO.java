package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.UserData;

public interface IUserDAO {
    void createUser(UserData user) throws DataAccessException;
    UserData checkPassword(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
