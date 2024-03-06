package dataAccess.SqlAccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.Interfaces.IUserDAO;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SQLUserDAO implements IUserDAO {
    private final String INSERT_USER_QUERY = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    private final String SELECT_USER_QUERY = "SELECT * FROM users WHERE username = ?";
    private final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";
    private static SQLUserDAO userDAO;
    public static SQLUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new SQLUserDAO();
        }
        return userDAO;
    }


    public void createUser(UserData user) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_USER_QUERY);
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData checkPassword(UserData user) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY);
            statement.setString(1, user.username());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserData foundUser = new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                if (!Objects.equals(foundUser.password(), user.password())) {
                    throw new DataAccessException("Password did not match");
                }
                return foundUser;
            } else {
                throw new DataAccessException("User not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
            } else {
                throw new DataAccessException("User does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(DELETE_ALL_USERS_QUERY);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
