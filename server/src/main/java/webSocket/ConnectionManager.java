package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authString, int gameId, Session session) {
        var connection = new Connection(authString, gameId, session);
        connections.put(authString, connection);
    }

    public void remove(String authString) {
        connections.remove(authString);
    }

    public void broadcast(String rootClientAuthString, int gameId, ServerMessage serverMessage) throws IOException {
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
}