package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
        import model.AuthData;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static int gameID;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    static void clear() {
        int i = 0;
        try {
            serverFacade.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterPositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email@email.com");
        AuthData authData = serverFacade.register(userData);
        assertNotNull(authData);
    }

    @Test
    public void testRegisterNegative() {
        assertThrows(ResponseException.class, () -> {
            UserData userData = new UserData("username", "password", "email");
            serverFacade.register(userData);
        });
    }

    @Test
    public void testLoginPositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        AuthData authData = serverFacade.login(userData);
        assertNotNull(authData);
        assertNotNull(authData.authToken());
    }

    @Test
    public void testLoginNegative() {
        UserData userData = new UserData("username", "wrongPassword", "email");
        assertThrows(ResponseException.class, () -> {
            serverFacade.login(userData);
        });
    }

    @Test
    public void testLogoutPositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        AuthData authData = serverFacade.login(userData);
        assertDoesNotThrow(() -> {
            serverFacade.logout(authData);
        });
    }

    @Test
    public void testLogoutNegative() {
        assertThrows(ResponseException.class, () -> {
            serverFacade.logout(new AuthData(null, "username"));
        });
    }
    @Test
    public void testCreateGamePositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        AuthData authData = serverFacade.login(userData);
        GameData gameData = new GameData(null, "username", null, "testGame", new ChessGame());
        gameData = serverFacade.createGame(authData, gameData);
        assertNotNull(gameData.gameID());
        this.gameID = gameData.gameID();
    }

    @Test
    public void testCreateGameNegative() {
        AuthData authData = null;
        GameData gameData = null;
        assertThrows(ResponseException.class, () -> serverFacade.createGame(authData, gameData));
    }

    @Test
    public void testListGamesPositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        AuthData authData = serverFacade.login(userData);
        Map<String, ArrayList<GameData>> gameMap = serverFacade.listGames(authData);
        assertNotNull(gameMap);
    }

    @Test
    public void testListGamesNegative() {
        assertThrows(ResponseException.class, () -> {
            serverFacade.listGames(new AuthData(null, "username"));
        });
    }

    @Test
    public void testJoinGamePositive() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        AuthData authData = serverFacade.login(userData);

        JoinGameBody.Color color = JoinGameBody.Color.BLACK;
        assertDoesNotThrow(() -> serverFacade.joinGame(authData, this.gameID, color));
    }

    @Test
    public void testJoinGameNegative() {
        AuthData authData = null;
        int gameId = -1;
        JoinGameBody.Color color = null;
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(authData, gameId, color));
    }

}
