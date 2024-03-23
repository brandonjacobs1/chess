package service;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.SqlAccess.SQLGameDAO;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import server.BadRequestException;
import server.DuplicateEntryException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {
    IGameDAO gameDAO;
    public GameService() {
        gameDAO = SQLGameDAO.getInstance();
    }
    public ArrayList<GameData> listGames()  {
        try {
            HashMap<Integer, GameData> games = gameDAO.listGames();
            return new ArrayList<>(games.values());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }
    }
    public GameData createGame(GameData game) throws DataAccessException {
        game = new GameData(game.gameID(), null, null, game.gameName(), null);
        game = gameDAO.createGame(game);
        return game;
    }
    public void joinGame(UserData user, int gameID, JoinGameBody.Color color) throws DuplicateEntryException, BadRequestException, DataAccessException {
        GameData game = gameDAO.getGame(gameID);
        ChessGame chessGame = game.game();
        if (chessGame == null || chessGame.getBoard() == null) {
            chessGame = new ChessGame();
            chessGame.setBoard(new ChessBoard());
            chessGame.getBoard().resetBoard();
        }
        if (color == JoinGameBody.Color.WHITE && game.whiteUsername() == null) {
            game = new GameData(gameID, user.username(), game.blackUsername(), game.gameName(), chessGame);
        } else if (color == JoinGameBody.Color.BLACK && game.blackUsername() == null) {
            game = new GameData(gameID, game.whiteUsername(), user.username(), game.gameName(), chessGame);
        } else if (color == null && game.whiteUsername() == null && game.blackUsername() == null && game.whiteUsername() == null){
            game = new GameData(gameID, null, null, game.gameName(), chessGame);
        } else {
            throw new DuplicateEntryException("bad color");
        }
        gameDAO.updateGame(game);
    }
    public void clear() throws DataAccessException {
        gameDAO.clear();
    }
}
