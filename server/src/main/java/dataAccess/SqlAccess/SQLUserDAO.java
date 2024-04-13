package dataAccess.SqlAccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.Interfaces.IUserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements IUserDAO {
    private final String insertUserQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    private final String selectUserQuery = "SELECT * FROM users WHERE username = ?";
    private final String deleteAllUsersQuery = "DELETE FROM users";
    private static SQLUserDAO userDAO;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static SQLUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new SQLUserDAO();
        }
        return userDAO;
    }


    public void createUser(UserData user) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        String hashedPassword = encoder.encode(user.password());
        try {
            PreparedStatement statement = conn.prepareStatement(insertUserQuery);
            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData checkPassword(UserData user) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(selectUserQuery);
            statement.setString(1, user.username());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserData foundUser = new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                boolean passwordMatch = encoder.matches(user.password(), foundUser.password());
                if (passwordMatch) {
                    return foundUser;
                }
                throw new DataAccessException("Password did not match");
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
            PreparedStatement statement = conn.prepareStatement(selectUserQuery);
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
            PreparedStatement statement = conn.prepareStatement(deleteAllUsersQuery);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
