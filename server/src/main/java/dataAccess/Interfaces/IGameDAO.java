package dataAccess.Interfaces;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public interface IGameDAO {
    public GameData createGame() throws DataAccessException;
    public GameData joinGame() throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;
    public GameData updateGame() throws DataAccessException;
    public void deleteGame();
    public void clear();


}
