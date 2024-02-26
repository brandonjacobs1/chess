package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IAuthDAO {
    public AuthData upsertAuth(UserData user);
    public void deleteAuth() throws DataAccessException;
    public void clear();
}
