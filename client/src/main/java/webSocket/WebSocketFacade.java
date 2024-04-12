package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import server.ResponseException;
import ui.ChessBoardUI;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;


    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> loadGame(message);
                        case ERROR -> error(message, session);
                        case NOTIFICATION -> notification(message, session);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr);
    }

    public void send(UserGameCommand msg) throws IOException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    public void error(String message, Session session) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
    }

    public void loadGame(String message) {
        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        ChessBoardUI ui = new ChessBoardUI(loadGameMessage.getGame());
        if (loadGameMessage.getTeamColor() == ChessGame.TeamColor.WHITE) {
            serverMessageHandler.showLoadGameMessage(ui.printWhite());
        } else if (loadGameMessage.getTeamColor() == ChessGame.TeamColor.BLACK) {
            serverMessageHandler.showLoadGameMessage(ui.printBlack());
        } else {
            serverMessageHandler.showLoadGameMessage(ui.printWhite());
        }
    }

    public void notification(String message, Session session) {
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        serverMessageHandler.showNotificationMessage(notificationMessage);
    }

    public void joinPlayer(String authToken, int gameId, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            var command = new JoinPlayerCommand(authToken, gameId, teamColor);
            send(command);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinObserver(String authToken, int gameId) throws ResponseException {
        try {
            var command = new JoinObserverCommand(authToken, gameId);
            send(command);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
