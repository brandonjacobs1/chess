package webSocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.SqlAccess.SQLGameDAO;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.BadRequestException;
import server.NotAuthenticatedException;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;


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

    @OnWebSocketError
    public void onError(Throwable error) {
        System.out.println("Error: " + error.getMessage());
    }


    private UserData authenticate(String authToken) throws WebSocketException {
        if (authToken == null) {
            throw new WebSocketException("No auth token provided");
        } else {
            try {
                UserData user = userService.getUser(authToken);
                if (user != null) {
                    return user;
                } else {
                    throw new WebSocketException("Not authenticated");
                }
            } catch (NotAuthenticatedException e) {
                throw new WebSocketException("Not authenticated");
            }
        }
    }


    public void joinPlayer(String message, Session session) throws IOException {
        JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
        Connection conn = new Connection(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), session);
        try {
            UserData user = authenticate(joinPlayerCommand.getAuthString());
            GameData gameData;
            try {
                gameData = gameDAO.getGame(joinPlayerCommand.getGameID());
            } catch (DataAccessException | BadRequestException e) {
                throw new WebSocketException("Error loading game");
            }
            if (gameData.game() == null) {
                throw new WebSocketException("Game has not been created");
            }
            if (Objects.equals(gameData.whiteUsername(), user.username()) && joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                throw new WebSocketException("White player cannot join as black");
            } else if (Objects.equals(gameData.blackUsername(), user.username()) && joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                throw new WebSocketException("Black player cannot join as white");
            }

            boolean isConnectionAvailable = connections.checkConnectionAvailable(joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), joinPlayerCommand.getAuthString());
            if (!isConnectionAvailable) {
                connections.sessionReply(conn, new ErrorMessage("ERROR: A player is already connected with this color"));
            } else {
                connections.add(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), session);
                var notification = new NotificationMessage(user.username() + " joined the game as the " + joinPlayerCommand.getPlayerColor() + " player");
                connections.broadcastNonRootClient(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), notification);
                LoadGameMessage loadGameMessage = loadGame(joinPlayerCommand.getGameID(), user.username());
                connections.reply(joinPlayerCommand.getAuthString(), loadGameMessage);
            }
        } catch (WebSocketException e) {
            connections.sessionReply(conn, new ErrorMessage("ERROR: " + e.getMessage()));
        }
    }
    private void joinObserver(String message, Session session) throws IOException {
        JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);
        Connection conn = new Connection(joinObserverCommand.getAuthString(), joinObserverCommand.getGameID(), null, session);
        try {
            UserData user = authenticate(joinObserverCommand.getAuthString());
            connections.add(joinObserverCommand.getAuthString(), joinObserverCommand.getGameID(), null, session);
            var notification = new NotificationMessage(user.username() + " joined the game as an observer");
            connections.broadcastNonRootClient(joinObserverCommand.getAuthString(), joinObserverCommand.getGameID(), notification);
            LoadGameMessage loadGameMessage = loadGame(joinObserverCommand.getGameID(), user.username());
            connections.reply(joinObserverCommand.getAuthString(), loadGameMessage);
        } catch (WebSocketException e) {
            connections.sessionReply(conn, new ErrorMessage("ERROR: " + e.getMessage()));
        }
    }
    private void move(String message, Session session) throws IOException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        try {
            UserData user = authenticate(makeMoveCommand.getAuthString());
            int gameId = makeMoveCommand.getGameID();
            GameData gameData;
            try {
                gameData = gameDAO.getGame(gameId);
            } catch (DataAccessException | BadRequestException e) {
                throw new WebSocketException("Error loading game");
            }
            if (gameData.game().isComplete()) {
                throw new WebSocketException("Game has ended");
            }
            ChessGame.TeamColor teamColor = connections.getTeamColor(makeMoveCommand.getAuthString());
            ChessGame chessGame = gameData.game();
            if (teamColor == chessGame.getTeamTurn()) {
                try {
                    chessGame.makeMove(makeMoveCommand.getMove());
                } catch (InvalidMoveException e) {
                    throw new WebSocketException("Invalid move");
                }
            } else {
                ErrorMessage errorMessage = new ErrorMessage("It is not your turn");
                connections.reply(makeMoveCommand.getAuthString(), errorMessage);
                return;
            }

            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
            try {
                gameDAO.updateGame(gameData);
            } catch (DataAccessException | BadRequestException e) {
                throw new WebSocketException("Error updating game");
            }
            boolean isInCheck = chessGame.isInCheck(chessGame.getTeamTurn());
            boolean isCheckmate = chessGame.isInCheckmate(chessGame.getTeamTurn());

            LoadGameMessage loadGameMessage = loadGame(gameId, user.username());
            connections.broadcastAll(makeMoveCommand.getGameID(), loadGameMessage);
            NotificationMessage notification = new NotificationMessage(user.username() + " moved " + makeMoveCommand.getMove().toString());
            connections.broadcastNonRootClient(makeMoveCommand.getAuthString(), makeMoveCommand.getGameID(), notification);

            if (isCheckmate) {
                gameService.completeGame(gameData);
                connections.broadcastAll(makeMoveCommand.getGameID(), new NotificationMessage("Checkmate! " + user.username() + " has won the game."));
            } else if (isInCheck) {
                connections.broadcastAll(makeMoveCommand.getGameID(), new NotificationMessage("Check! " + user.username() + " is in check."));
            }
        } catch (Exception e) {
            connections.reply(makeMoveCommand.getAuthString(), new ErrorMessage("ERROR: " + e.getMessage()));
        }
    }

    private ChessGame.TeamColor getTeamColor(UserData user, GameData gameData) throws WebSocketException {
        ChessGame.TeamColor teamColor;
        if (user.username().equals(gameData.whiteUsername())) {
            teamColor = ChessGame.TeamColor.WHITE;
        } else if (user.username().equals(gameData.blackUsername())) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else {
            throw new WebSocketException("User is not a player in this game");
        }
        return teamColor;
    }

    private void leave(String message, Session session) throws IOException {
        LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
        try {
            UserData user = authenticate(leaveCommand.getAuthString());
            int gameId = leaveCommand.getGameID();
            GameData gameData;
            try {
                gameData = gameDAO.getGame(gameId);
            } catch (Exception e) {
                throw new WebSocketException("Error loading game");
            }
            gameService.removePlayer(gameData, user.username());
            gameService.completeGame(gameData);
            NotificationMessage notification = new NotificationMessage(user.username() + " has left the game");
            connections.broadcastNonRootClient(leaveCommand.getAuthString(), leaveCommand.getGameID(), notification);
            session.close();
        } catch (WebSocketException e) {
            connections.reply(leaveCommand.getAuthString(), new ErrorMessage("ERROR: " + e.getMessage()));
        }
    }
    private void resign(String message, Session session) throws IOException {
        ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
        try {
            UserData user = authenticate(resignCommand.getAuthString());
            GameData gameData;
            try {
                gameData = gameDAO.getGame(resignCommand.getGameID());
            } catch (DataAccessException | BadRequestException e) {
                throw new WebSocketException("Error loading game");
            }
            if (gameData.game().isComplete()) {
                throw new WebSocketException("Game has already ended");
            }
            ChessGame.TeamColor teamColor = connections.getTeamColor(resignCommand.getAuthString());
            if (teamColor == null) {
                throw new WebSocketException("User is an observer and cannot resign the game");
            }
            gameService.completeGame(gameData);
            connections.broadcastAll(resignCommand.getGameID(), new NotificationMessage(user.username() + " has resigned the game."));
//            connections.closeAllConnections(resignCommand.getGameID());
        } catch (WebSocketException e) {
            connections.reply(resignCommand.getAuthString(), new ErrorMessage("ERROR: " + e.getMessage()));
        }
    }

    private LoadGameMessage loadGame(int gameId, String username) throws IOException, WebSocketException {
        GameData gameData;
        try {
            gameData = gameDAO.getGame(gameId);
        } catch (DataAccessException | BadRequestException e) {
            throw new WebSocketException("Error loading game");
        }
        return new LoadGameMessage(gameData, username);
    }
}
