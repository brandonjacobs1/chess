package dataAccess.SqlAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.Interfaces.IGameDAO;
import model.GameData;
import server.BadRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLGameDAO implements IGameDAO {
    private static final String INSERT_GAME_QUERY = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_GAME_QUERY = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
    private static final String SELECT_GAME_QUERY = "SELECT * FROM game WHERE gameID = ?";
    private static final String SELECT_ALL_GAMES_QUERY = "SELECT * FROM game";
    private static final String DELETE_ALL_GAMES_QUERY = "DELETE FROM game";
    Gson serializer = new Gson();

    private static SQLGameDAO gameDAO;

    public static SQLGameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new SQLGameDAO();
        }
        return gameDAO;
    }

    public GameData createGame(GameData game) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_GAME_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, serializer.toJson(game.game()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int gameId = resultSet.getInt(1);
                return getGame(gameId);
            } else {
                throw new DataAccessException("Game not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGame(GameData game) throws BadRequestException, DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(UPDATE_GAME_QUERY);
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, serializer.toJson(game.game()));
            statement.setInt(5, game.gameID());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new BadRequestException("Game ID does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public HashMap<Integer, GameData> listGames() {
        HashMap<Integer, GameData> games = new HashMap<>();
        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(SELECT_ALL_GAMES_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int gameId = resultSet.getInt("gameId");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String gameName = resultSet.getString("gameName");
                ChessGame chessGame = serializer.fromJson(resultSet.getString("game"), ChessGame.class);
                GameData game = new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
                games.put(gameId, game);
            }
            return games;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameID) throws BadRequestException, DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(SELECT_GAME_QUERY);
            statement.setInt(1, gameID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String gameName = resultSet.getString("gameName");
                ChessGame chessGame = serializer.fromJson(resultSet.getString("game"), ChessGame.class);
                return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
            } else {
                throw new BadRequestException("Game not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void clear() {
        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(DELETE_ALL_GAMES_QUERY);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
