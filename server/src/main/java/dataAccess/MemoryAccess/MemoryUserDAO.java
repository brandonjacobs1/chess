package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IUserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class MemoryUserDAO implements IUserDAO {
    HashMap<String, UserData> users = new HashMap<>();
    public UserData createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User Already Exists");
        }
        users.put(user.username(), user);
        return user;
    }

    public UserData updateUser(UserData user) {
        return null;
    }

    public UserData getUser(UserData user) throws DataAccessException {
        UserData foundUser = users.get(user.username());
        if (foundUser == null) {
            throw new DataAccessException("User not found");
        } else if (!Objects.equals(foundUser.password(), user.password())) {
            throw new DataAccessException("Password did not match");
        }
        return foundUser;
    }

    public void deleteUser() {

    }

    public void clear() {
        users.clear();
    }
}
