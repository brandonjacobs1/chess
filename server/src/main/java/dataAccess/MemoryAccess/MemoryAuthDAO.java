package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.*;

public class MemoryAuthDAO implements IAuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();
    public AuthData createAuth(UserData user) {
        String authToken = UUID.randomUUID().toString();
        auths.put(user.username(), new AuthData(authToken, user.username()));
        return auths.get(user.username());
    }

    public AuthData getAuth(String token){
        String username = getUsernameByToken(token);
        return auths.get(username);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String usernameToRemove = getUsernameByToken(authToken);

        if (usernameToRemove != null) {
            auths.remove(usernameToRemove);
        }
    }

    public void clear() {
        auths.clear();
    }

    private String getUsernameByToken(String token){
        String usernameToRemove = null;
        for (Map.Entry<String, AuthData> entry : auths.entrySet()) {
            if (entry.getValue().authToken().equals(token)) {
                usernameToRemove = entry.getKey();
                break;
            }
        }
        return usernameToRemove;

    }
}
