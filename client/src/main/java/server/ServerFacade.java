package server;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.JoinGameBody;
import model.UserData;

public class ServerFacade {
    private ApiCall apiManager;

    public ServerFacade(String url) {
        this.apiManager = new ApiCall(url);
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        Type t = new TypeToken<AuthData>(){}.getType();
        return apiManager.makeRequest("POST", path, user, t, null);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return apiManager.makeRequest("POST", path, user, new TypeToken<AuthData>(){}.getType(), null);
    }

    public void logout(AuthData auth) throws ResponseException {
        var path = "/session";
        apiManager.makeRequest("DELETE", path, null, null, auth);
    }

    public Map<String, ArrayList<GameData>> listGames(AuthData auth) throws ResponseException {
        var path = "/game";
        Type t = new TypeToken<Map<String, ArrayList<GameData>>>(){}.getType();
        return apiManager.makeRequest("GET", path, null, t, auth);
    }

    public GameData joinGame(AuthData auth, int gameID, JoinGameBody.Color color) throws ResponseException {
        var path = "/game";
        return apiManager.makeRequest("PUT", path, new JoinGameBody(color, gameID), null, auth);
    }

    public GameData createGame(AuthData auth, GameData game) throws ResponseException {
        var path = "/game";
        return apiManager.makeRequest("POST", path, game, new TypeToken<GameData>(){}.getType(), auth);
    }
}