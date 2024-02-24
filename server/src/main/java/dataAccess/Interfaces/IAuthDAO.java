package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    public AuthData createAuth(UserData user) throws DataAccessException;
    public AuthData getAuth() throws DataAccessException;
    public void deleteAuth() throws DataAccessException;
    public void clear();
}
