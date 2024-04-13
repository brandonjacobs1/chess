package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authString, int gameId, ChessGame.TeamColor teamColor, Session session) {
        var connection = new Connection(authString, gameId, teamColor, session);
        connections.put(authString, connection);
    }

    public void remove(String authString) {
        connections.remove(authString);
    }

    public void broadcastNonRootClient(String rootClientAuthString, int gameId, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (gameId == c.gameId) {
                    if (!c.authString.equals(rootClientAuthString)) {
                        c.send(new Gson().toJson(serverMessage));
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authString);
        }
    }

    public void broadcastAll(int gameId, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (gameId == c.gameId) {
                    c.send(new Gson().toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authString);
        }
    }

    public void reply(String authString, ServerMessage serverMessage) throws IOException {
        var connection = connections.get(authString);
        if (connection.session.isOpen()) {
            connection.send(new Gson().toJson(serverMessage));
        } else {
            connections.remove(authString);
        }
    }

    public void sessionReply(Connection connection, ServerMessage serverMessage) throws IOException {
        if (connection.session.isOpen()) {
            connection.send(new Gson().toJson(serverMessage));
        }
    }

    public void closeAllConnections(int gameId) {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.gameId == gameId) {
                if (c.session.isOpen()) {
                    c.session.close();
                }
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.authString);
        }
    }

    public boolean checkConnectionAvailable(int gameId, ChessGame.TeamColor teamColor, String authString) {
        for (var c : connections.values()) {
            if (c.gameId == gameId) {
                if (c.authString.equals(authString)) {
                    return false;
                } else if (c.teamColor == teamColor) {
                    return false;
                }
            }
        }
        return true;

    }

    public ChessGame.TeamColor getTeamColor(String authString) {
        return connections.get(authString).teamColor;
    }
}
