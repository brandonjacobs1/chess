package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.*;

public class MemoryAuthDAO implements IAuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();
    public AuthData upsertAuth(UserData user) {
        String authToken = UUID.randomUUID().toString();
        auths.put(user.username(), new AuthData(authToken, user.username()));
        return auths.get(user.username());
    }

    public void deleteAuth() throws DataAccessException {

    }

    public void clear() {

    }
}
