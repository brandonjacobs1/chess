package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    public AuthData createAuth(UserData user);
    public AuthData getAuth(String token);
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear();
}
