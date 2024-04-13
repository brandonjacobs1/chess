package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.SqlAccess.SQLGameDAO;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.BadRequestException;
import server.NotAuthenticatedException;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;



@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService = new UserService();
    private final IGameDAO gameDAO = SQLGameDAO.getInstance();
    private final GameService gameService = new GameService();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case MAKE_MOVE -> move(message, session);
            case LEAVE -> leave(message, session);
            case RESIGN -> resign(message, session);
        }
    }

    private UserData authenticate(String authToken) {
        if (authToken == null) {
            throw new RuntimeException("No auth token provided");
        } else {
            try {
                UserData user = userService.getUser(authToken);
                if (user != null) {
                    return user;
                } else {
                    throw new RuntimeException("Not authenticated");
                }
            } catch (NotAuthenticatedException e) {
                throw new RuntimeException("Not authenticated");
            }
        }
    }
    public void joinPlayer(String message, Session session) throws IOException {
        JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
        UserData user = authenticate(joinPlayerCommand.getAuthString());
        connections.add(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameId(), session);
        var notification = new NotificationMessage(user.username() + " joined the game as the " + joinPlayerCommand.getTeamColor() + " player");
        connections.broadcastNonRootClient(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameId(), notification);
        LoadGameMessage loadGameMessage = loadGame(joinPlayerCommand.getGameId(), joinPlayerCommand.getTeamColor());
        connections.reply(joinPlayerCommand.getAuthString(), loadGameMessage);
    }
    private void joinObserver(String message, Session session) throws IOException {
        JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);
        UserData user = authenticate(joinObserverCommand.getAuthString());
        connections.add(joinObserverCommand.getAuthString(), joinObserverCommand.getGameId(), session);
        var notification = new NotificationMessage(user.username() + " joined the game as an observer");
        connections.broadcastNonRootClient(joinObserverCommand.getAuthString(), joinObserverCommand.getGameId(), notification);
        LoadGameMessage loadGameMessage = loadGame(joinObserverCommand.getGameId(), null);
        connections.reply(joinObserverCommand.getAuthString(), loadGameMessage);
    }
    private void move(String message, Session session) throws IOException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
    }
    private void leave(String message, Session session) throws IOException {
        LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
        UserData user = authenticate(leaveCommand.getAuthString());
        int gameId = leaveCommand.getGameId();
        GameData gameData;
        try {
            gameData = gameDAO.getGame(gameId);
        } catch (Exception e) {
            throw new RuntimeException("Error loading game");
        }
        gameService.removePlayer(gameData, user.username());
        gameService.completeGame(gameData);
        NotificationMessage notification = new NotificationMessage(user.username() + " has left the game");
        connections.broadcastNonRootClient(leaveCommand.getAuthString(), leaveCommand.getGameId(), notification);
        session.close();
    }
    private void resign(String message, Session session) throws IOException {
        ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
        UserData user = authenticate(resignCommand.getAuthString());
        GameData gameData;
        try {
            gameData = gameDAO.getGame(resignCommand.getGameId());
        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException("Error loading game");
        }
        gameService.completeGame(gameData);
        connections.broadcastAll(resignCommand.getAuthString(), resignCommand.getGameId(), new NotificationMessage(user.username() + " has resigned the game."));
        connections.closeAllConnections(resignCommand.getGameId());
    }

    private LoadGameMessage loadGame(int gameId, ChessGame.TeamColor color) {
        GameData gameData;
        try {
            gameData = gameDAO.getGame(gameId);
        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException("Error loading game");
        }
        ChessGame game = gameData.game();
        return new LoadGameMessage(game, color);
    }
}
