package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IUserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements IUserDAO {
    HashMap<String, UserData> users = new HashMap<>();

    private static MemoryUserDAO userDAO;

    public static MemoryUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new MemoryUserDAO();
        }
        return userDAO;
    }

    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User Already Exists");
        }
        users.put(user.username(), user);
    }

    public UserData checkPassword(UserData user) throws DataAccessException {
        UserData foundUser = users.get(user.username());
        if (foundUser == null) {
            throw new DataAccessException("User not found");
        } else if (!Objects.equals(foundUser.password(), user.password())) {
            throw new DataAccessException("Password did not match");
        }
        return foundUser;
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user == null) {
            throw new DataAccessException("User does not exist");
        } else {
            return user;
        }
    }

    public void clear() {
        users.clear();
    }
}
