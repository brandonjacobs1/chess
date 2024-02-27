package service;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.MemoryAccess.MemoryGameDAO;
import model.GameData;
import model.JoinGameBody;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {
    IGameDAO gameDAO;
    public GameService() {
        this.gameDAO = new MemoryGameDAO();
    }
    public ArrayList<GameData> listGames() throws DataAccessException {
        HashMap<Integer,GameData> games =  gameDAO.listGames();
        return new ArrayList<>(games.values());
    };
    public GameData createGame(GameData game, UserData user) throws DataAccessException {
        game = new GameData(null, null, null, game.gameName(), null);
        game = gameDAO.createGame(game);
        return game;
    };
    public void joinGame(UserData user, int gameID, JoinGameBody.Color color) throws Exception {
        GameData game = gameDAO.getGame(gameID);
        if (color == JoinGameBody.Color.WHITE && game.whiteUsername() == null) {
            game = new GameData(gameID, user.username(), game.blackUsername(), game.gameName(), game.game());
        } else if (color == JoinGameBody.Color.BLACK && game.blackUsername() == null) {
            game = new GameData(gameID, game.whiteUsername(), user.username(), game.gameName(), game.game());
        } else {
            throw new Exception("new exception");
        }
        gameDAO.updateGame(game);
    };
    public void clear() {
        gameDAO.clear();
    }
}
