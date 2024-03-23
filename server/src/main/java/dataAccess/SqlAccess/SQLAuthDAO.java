package dataAccess.SqlAccess;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import dataAccess.DatabaseManager;

public class SQLAuthDAO implements IAuthDAO {

    private static final String INSERT_AUTH_QUERY = "INSERT INTO auth (token, username) VALUES (?, ?)";
    private static final String SELECT_AUTH_QUERY = "SELECT * FROM auth WHERE token = ?";
    private static final String DELETE_AUTH_QUERY = "DELETE FROM auth WHERE token = ?";

    private static SQLAuthDAO authDAO;
    public static SQLAuthDAO getInstance() {
        if (authDAO == null) {
            authDAO = new SQLAuthDAO();
        }
        return authDAO;
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(INSERT_AUTH_QUERY)) {
            statement.setString(1, authToken);
            statement.setString(2, user.username());
            statement.executeUpdate();
            return new AuthData(authToken, user.username());
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String token) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(SELECT_AUTH_QUERY);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new AuthData(resultSet.getString("token"), resultSet.getString("username"));
            } else {
                throw new DataAccessException("Auth not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_AUTH_QUERY)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String DELETE_ALL_QUERY = "DELETE FROM auth";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_ALL_QUERY)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }}
