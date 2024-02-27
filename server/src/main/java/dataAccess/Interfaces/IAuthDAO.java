package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    AuthData createAuth(UserData user);
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(String authToken);
    void clear();
}
