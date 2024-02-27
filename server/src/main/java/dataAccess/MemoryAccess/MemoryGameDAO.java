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
        int gameID = ++counter;
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
    public void updateGame(GameData game) throws BadRequestException {
        if (games.containsKey(game.gameID())) {
            games.put(game.gameID(), game);
        } else {
            throw new BadRequestException("Game ID does not exist");
        }
    }

    public HashMap<Integer, GameData> listGames() throws DataAccessException{
        if (games == null) {
            throw new DataAccessException("There are no games");
        } else {
            return games;
        }
    }
    public GameData getGame(int gameID) throws BadRequestException {
        GameData game =  games.get(gameID);
        if (game == null) {
            throw new BadRequestException("Game not found");
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
