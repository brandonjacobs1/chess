package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.*;

public class MemoryAuthDAO implements IAuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();

    private static MemoryAuthDAO authDAO;
    public static MemoryAuthDAO getInstance() {
        if (authDAO == null) {
            authDAO = new MemoryAuthDAO();
        }
        return authDAO;
    }
    public AuthData createAuth(UserData user) {
        String authToken = UUID.randomUUID().toString();
        auths.put(authToken, new AuthData(authToken, user.username()));
        return auths.get(authToken);
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData auth = auths.get(token);
        if (auth == null) {
            throw new DataAccessException("User not found");
        }
        return auth;
    }

    public void deleteAuth(String authToken) {
         auths.remove(authToken);
    }

    public void clear() {
        auths.clear();
    }

}
