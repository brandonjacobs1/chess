package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private static final String TEST_USERNAME = "test_user";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_EMAIL = "test@example.com";

    private SQLUserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = SQLUserDAO.getInstance();
    }
    @AfterEach
    public void tearDown() {
        try {
            userDAO.clear();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateUser_Positive() {
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            userDAO.createUser(user);
            UserData retrievedUser = userDAO.getUser(TEST_USERNAME);
            assertNotNull(retrievedUser);
            assertEquals(TEST_USERNAME, retrievedUser.username());
            assertEquals(TEST_EMAIL, retrievedUser.email());
        } catch (DataAccessException e) {
            fail("Failed to create user: " + e.getMessage());
        }
    }

    @Test
    public void testCreateUser_Negative() {
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            userDAO.createUser(user);
            assertThrows(DataAccessException.class, () -> {
                userDAO.createUser(user);
            });
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("Duplicate"));
        }
    }

    @Test
    public void testCheckPassword_Positive() {
        // Positive test case for checking user password
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            // Create the user
            userDAO.createUser(user);
            // Check password for the created user
            UserData checkedUser = userDAO.checkPassword(user);
            assertNotNull(checkedUser);
            assertEquals(TEST_USERNAME, checkedUser.username());
            assertEquals(TEST_EMAIL, checkedUser.email());
        } catch (DataAccessException e) {
            fail("Failed to check password: " + e.getMessage());
        }
    }

    @Test
    public void testCheckPassword_Negative() {
        // Negative test case for checking password with incorrect password
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            // Create the user
            userDAO.createUser(user);
            // Attempt to check password with incorrect password
            UserData incorrectPasswordUser = new UserData(TEST_USERNAME, "incorrect_password", TEST_EMAIL);
            userDAO.checkPassword(incorrectPasswordUser);
            fail("Expected DataAccessException to be thrown for incorrect password");
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("Password did not match")); // Expecting password mismatch error
        }
    }

    @Test
    public void testGetUser_Positive() {
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            userDAO.createUser(user);
            UserData retrievedUser = userDAO.getUser(TEST_USERNAME);
            assertNotNull(retrievedUser);
            assertEquals(TEST_USERNAME, retrievedUser.username());
            assertEquals(TEST_EMAIL, retrievedUser.email());
        } catch (DataAccessException e) {
            fail("Failed to retrieve user: " + e.getMessage());
        }
    }

    @Test
    public void testGetUser_Negative() {
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(TEST_USERNAME);
        });
    }

    @Test
    public void testClear_Positive() {
        UserData user = new UserData(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        try {
            userDAO.createUser(user);
            userDAO.clear();
            assertThrows(DataAccessException.class, () -> {
                userDAO.getUser(TEST_USERNAME);
            });
        } catch (DataAccessException e) {
            fail("Failed to clear users: " + e.getMessage());
        }
    }
}
