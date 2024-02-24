package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IUserDAO;
import model.UserData;
import service.UserService;

import java.util.ArrayList;

public class MemoryUserDAO implements IUserDAO {
    ArrayList<UserData> users = new ArrayList<>();
    public UserData createUser(UserData user) throws DataAccessException {
        for(UserData existingUser : users) {
            if (existingUser.equals(user)){
                throw new DataAccessException("User Already Exists");
            }
        }
        users.add(user);
        return user;
    }

    public UserData updateUser(UserData user) {
        return null;
    }

    public UserData getUser() {
        return null;
    }

    public void deleteUser() {

    }

    public void clear() {

    }
}
