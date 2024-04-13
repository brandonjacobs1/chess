package webSocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authString;
    public Session session;
    public int gameId;

    public ChessGame.TeamColor teamColor;

    public Connection(String authString, int gameId, ChessGame.TeamColor teamColor, Session session) {
        this.authString = authString;
        this.session = session;
        this.gameId = gameId;
        this.teamColor = teamColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}