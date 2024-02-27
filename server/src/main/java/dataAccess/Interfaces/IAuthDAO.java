package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import dataAccess.MemoryAccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    AuthData createAuth(UserData user);
    AuthData getAuth(String token);
    void deleteAuth(String authToken) throws DataAccessException;
    void clear();
}
