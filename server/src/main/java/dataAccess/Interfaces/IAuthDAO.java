package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
