package serviceTests;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.DuplicateEntryException;
import server.NotAuthenticatedException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("register success")
    public void testRegisterPositive() throws DuplicateEntryException {
        // Arrange
        UserData newUser = new UserData("testUser", "testPassword", "testEmail");

        // Act
        AuthData authData = userService.register(newUser);

        // Assert
        assertNotNull(authData);
    }

    @Test
    @DisplayName("register failure")
    public void testRegisterNegative() {
        // Arrange: Create a user that already exists
        UserData existingUser = new UserData("testUser", "testPassword", "testEmail");

        // Act & Assert
        assertThrows(DuplicateEntryException.class, () -> {
            userService.register(existingUser);
        });
    }

    @Test
    @DisplayName("login success")
    public void testLoginPositive() throws NotAuthenticatedException, DuplicateEntryException {
        // Arrange: Create a user and register them
        UserData newUser = new UserData("existingUser", "existingPassword", "existingEmail");
        userService.register(newUser);

        // Act
        AuthData authData = userService.login(newUser);

        // Assert
        assertNotNull(authData);
    }

    @Test
    @DisplayName("logout failure")
    public void testLoginNegative() {
        // Arrange: Create a user that doesn't exist
        UserData nonExistentUser = new UserData("nonExistentUser", "nonExistentPassword", "nonExistentEmail");

        // Act & Assert
        assertThrows(NotAuthenticatedException.class, () -> {
            userService.login(nonExistentUser);
        });
    }

    @Test
    @DisplayName("logout success")
    public void testLogoutPositive() {
        // Arrange: Auth token of an existing user
        String authToken = "validAuthToken";

        // Act
        assertDoesNotThrow(() -> {
            userService.logout(authToken);
        });
    }

    @Test
    @DisplayName("authenticate success")
    public void testAuthenticatePositive() throws NotAuthenticatedException, DuplicateEntryException {
        // Arrange: Auth token of an existing user
        UserData newUser = new UserData("testUser", "testPassword", "testEmail");
        AuthData auth = userService.register(newUser);
        boolean isAuthenticated = userService.authenticate(auth.authToken());

        assertTrue(isAuthenticated);
        userService.clear();
    }

    @Test
    @DisplayName("authenticate failure")
    public void testAuthenticateNegative() {
        // Arrange: Auth token of a non-existent user
        String nonExistentToken = "invalidAuthToken";

        // Act & Assert
        assertThrows(NotAuthenticatedException.class, () -> {
            userService.authenticate(nonExistentToken);
        });
    }

    @Test
    @DisplayName("getUser success")
    public void testGetUserPositive() throws NotAuthenticatedException {
        AuthData auth = userService.login(new UserData("testUser", "testPassword", "testEmail"));
        UserData user = userService.getUser(auth.authToken());

        // Assert
        assertNotNull(user);
    }

    @Test
    @DisplayName("getUser failure")
    public void testGetUserNegative() {
        // Arrange: Auth token of a non-existent user
        String nonExistentToken = "invalidAuthToken";

        // Act & Assert
        assertThrows(NotAuthenticatedException.class, () -> {
            userService.getUser(nonExistentToken);
        });
    }

    @Test
    @DisplayName("Clear success")
    public void testClearPositive() {
        // Act & Assert: Ensure that no exception is thrown
        assertDoesNotThrow(() -> {
            userService.clear();
        });
    }
}
