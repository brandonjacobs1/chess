package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlAccess.SQLAuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {

    private static final String TEST_USERNAME = "test_user";
    private static final String TEST_TOKEN = "test_token";

    private SQLAuthDAO authDAO;

    @Before
    public void setUp() {
        authDAO = SQLAuthDAO.getInstance();
    }

//    @After
//    public void tearDown() {
//        // Clean up any data created during testing
//        try {
//            authDAO.clear();
//        } catch (DataAccessException e) {
//            fail("Error occurred while clearing data: " + e.getMessage());
//        }
//    }

    @Test
    public void testCreateAuth_Positive() {
        UserData user = new UserData(TEST_USERNAME, "1234", "test@test.com");
        try {
            AuthData authData = authDAO.createAuth(user);
            assertNotNull(authData);
            assertEquals(TEST_USERNAME, authData.username());
            assertNotNull(authData.authToken());
        } catch (DataAccessException e) {
            fail("Failed to create authentication data: " + e.getMessage());
        }
    }

    @Test
    public void testCreateAuth_Negative() {
        UserData user = new UserData(null, "12345", "test@test.com");
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(user));
    }

    @Test
    public void testGetAuth_Positive() {
        UserData user = new UserData(TEST_USERNAME, "1234", "test@test.com");
        try {
            AuthData createdAuthData = authDAO.createAuth(user);
            assertNotNull(createdAuthData);
            String token = createdAuthData.authToken();
            AuthData retrievedAuthData = authDAO.getAuth(token);
            assertNotNull(retrievedAuthData);
            assertEquals(TEST_USERNAME, retrievedAuthData.username());
            assertEquals(token, retrievedAuthData.authToken());
        } catch (DataAccessException e) {
            fail("Failed to retrieve authentication data: " + e.getMessage());
        }
    }

    @Test
    public void testGetAuth_Negative() {
        // Negative test case for retrieving authentication data with invalid token
        try {
            AuthData retrievedAuthData = authDAO.getAuth(TEST_TOKEN);
            assertNull(retrievedAuthData); // Expecting null since token doesn't exist
        } catch (DataAccessException e) {
            // Expecting exception to be thrown due to token not found
            assertTrue(e.getMessage().contains("Auth not found"));
        }
    }

    @Test
    public void testDeleteAuth_Positive() {
        UserData user = new UserData(TEST_USERNAME, "1234", "test@test.com");
         try {
             AuthData createdAuthData = authDAO.createAuth(user);
             assertNotNull(createdAuthData);
             String token = createdAuthData.authToken();
             authDAO.deleteAuth(token);
             assertThrows(DataAccessException.class, () -> authDAO.getAuth(token));
         } catch (DataAccessException e) {
             fail("Failed to delete authentication data: " + e.getMessage());
         }
    }

    // No negative test case for deleteAuth method as it's straightforward deletion

    @Test
    public void testClear_Positive() {
        UserData user1 = new UserData(TEST_USERNAME + "1", "1234", "test@test.com");
        try {
            AuthData authData = authDAO.createAuth(user1);
            assertNotNull(authData);
            authDAO.clear();
            // Attempt to retrieve authentication data after clearing
            assertThrows(DataAccessException.class, () -> authDAO.getAuth(authData.authToken()));

        } catch (DataAccessException e) {
            fail("Failed to clear authentication data: " + e.getMessage());
        }
    }
}
