package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MemoryGameDAO implements IGameDAO {
    HashMap<Integer, GameData> games = new HashMap<>();
    private static MemoryGameDAO gameDAO;
    public static MemoryGameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new MemoryGameDAO();
        }
        return gameDAO;
    }
    int counter = 0;

    public MemoryGameDAO() {
        games = new HashMap<>();
        counter = 0;
    }
    public GameData createGame(GameData game) throws DataAccessException {
        int gameID = counter++;
        game = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        if (!games.containsKey(gameID)) {
            try {
                games.put(gameID, game);
            } catch (Exception e) {
                throw new RuntimeException("Could not create game");
            }
            return game;
        } else {
            throw new DataAccessException("Game already exists");
        }
    }
    public GameData updateGame(GameData game) {
        try {
            games.put(game.gameID(), game);
        } catch (Exception e) {
            throw new RuntimeException("Could not update game");
        }
        return game;
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException{
        if (games == null) {
            throw new DataAccessException("There are no games");
        } else {
            return games;
        }
    }
    public GameData getGame(int gameID) throws DataAccessException{
        GameData game =  games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found");
        }
        return game;
    }
//    public void deleteGame(GameData game){
//        games.remove(game.gameID());
//    }
    public void clear(){
        games.clear();
    }
}
