package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.MemoryAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.BadRequestException;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameService gameService;
    private IGameDAO gameDAO;

    @BeforeEach
    void setUp() {
        // Create an instance of MemoryGameDAO
        gameService = new GameService();
        gameDAO = MemoryGameDAO.getInstance();
    }

    @Test
    @DisplayName("Test listGames method with 2 games")
    void testListGamesPositive() throws DataAccessException {
        GameData game1 = new GameData(1, "WhitePlayer1", "BlackPlayer1", "Game1", null);
        GameData game2 = new GameData(2, "WhitePlayer2", "BlackPlayer2", "Game2", null);
        gameService.createGame(game1);
        gameDAO.createGame(game2);

        var games = gameService.listGames();

        assertEquals(2, games.size());
    }

    @Test
    @DisplayName("Test listGames method with no games")
    void testListGamesNegative() {
        gameDAO.clear();
        var games = gameService.listGames();
        assertTrue(games.isEmpty());
    }


    @Test
    @DisplayName("Test create game")
    public void testCreateGamePositive() throws DataAccessException {
        GameData gameData = new GameData(null, null, null, "Test Game", null);
        GameData game = gameService.createGame(gameData);
        assertNotNull(game);
    }

    @Test
    @DisplayName("Test create game failure")
    public void testCreateGameNegative() throws DataAccessException {
        GameData gameData = new GameData(null, null, null, "Test Game", null);
        gameService.createGame(gameData);
        gameData = new GameData(1, null, null, "Test Game", null);

        GameData finalGameData = gameData;
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(finalGameData);
        });
    }

    @Test
    public void testJoinGamePositive() throws DuplicateEntryException, BadRequestException, DataAccessException {
        // Positive test: Verify that the method successfully joins a user to a game
        // Implement the test logic according to the requirements
    }

    @Test
    public void testJoinGameNegative1() {
        // Negative test: Verify that the method throws DuplicateEntryException
        // Implement the test logic according to the requirements
    }

    @Test
    public void testJoinGameNegative2() {
        // Negative test: Verify that the method throws BadRequestException
        // Implement the test logic according to the requirements
    }

    @Test
    public void testJoinGameNegative3() {
        // Negative test: Verify that the method throws DataAccessException
        // Implement the test logic according to the requirements
    }

    @Test
    public void testClear() {
        // Positive test: Verify that the method clears the game data
        // Implement the test logic according to the requirements
    }
}
