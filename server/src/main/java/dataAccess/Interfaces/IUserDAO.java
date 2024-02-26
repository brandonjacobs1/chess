package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.UserData;

public interface IUserDAO {
    public UserData createUser(UserData user) throws DataAccessException;
    public UserData updateUser(UserData user) throws DataAccessException;
    public UserData getUser(UserData user) throws DataAccessException;
    public void deleteUser();
    public void clear();


}
