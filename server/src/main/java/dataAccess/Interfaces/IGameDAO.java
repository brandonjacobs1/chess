package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.GameData;
import server.BadRequestException;
import java.util.HashMap;

public interface IGameDAO {
    GameData createGame(GameData game) throws DataAccessException;
    void updateGame(GameData game) throws BadRequestException;
    HashMap<Integer, GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException, BadRequestException;
//    void deleteGame(GameData game);
    void clear();


}
