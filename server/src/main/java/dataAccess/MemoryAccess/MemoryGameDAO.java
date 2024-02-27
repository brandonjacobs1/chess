package dataAccess.MemoryAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import model.GameData;
import server.BadRequestException;
import java.util.HashMap;

public class MemoryGameDAO implements IGameDAO {
    HashMap<Integer, GameData> games;

    private static MemoryGameDAO gameDAO;
    public static MemoryGameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new MemoryGameDAO();
        }
        return gameDAO;
    }
    int counter;

    public MemoryGameDAO() {
        games = new HashMap<>();
        counter = 0;
    }
    public GameData createGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            throw new DataAccessException("Game already exists");
        }
        int gameID = ++counter;
        game = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        games.put(gameID, game);
        return game;
    }
    public void updateGame(GameData game) throws BadRequestException {
        if (games.containsKey(game.gameID())) {
            games.put(game.gameID(), game);
        } else {
            throw new BadRequestException("Game ID does not exist");
        }
    }

    public HashMap<Integer, GameData> listGames() {
            return games;
    }
    public GameData getGame(int gameID) throws BadRequestException {
        GameData game =  games.get(gameID);
        if (game == null) {
            throw new BadRequestException("Game not found");
        }
        return game;
    }
    public void clear(){
        games.clear();
    }
}
