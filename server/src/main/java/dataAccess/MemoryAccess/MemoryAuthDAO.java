package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    ArrayList<AuthData> auths = new ArrayList<>();
    public AuthData createAuth(UserData user) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        auths.add(authData);
        return authData;
    }

    public AuthData getAuth() throws DataAccessException {
        return null;
    }

    public void deleteAuth() throws DataAccessException {

    }

    public void clear() {

    }
}
