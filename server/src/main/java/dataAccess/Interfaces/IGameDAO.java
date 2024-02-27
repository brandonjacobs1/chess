package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public interface IGameDAO {
    GameData createGame(GameData game) throws DataAccessException;
    GameData updateGame(GameData game);
    HashMap<Integer, GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
//    void deleteGame(GameData game);
    void clear();


}
