package webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authString;
    public Session session;
    public int gameId;

    public Connection(String authString, int gameId, Session session) {
        this.authString = authString;
        this.session = session;
        this.gameId = gameId;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}