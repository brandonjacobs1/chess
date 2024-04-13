package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Interfaces.IGameDAO;
import dataAccess.MemoryAccess.MemoryGameDAO;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.BadRequestException;
import server.DuplicateEntryException;
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
        gameService.clear();
        GameData game1 = new GameData(null, "WhitePlayer1", "BlackPlayer1", "Game1", null);
        GameData game2 = new GameData(null, "WhitePlayer2", "BlackPlayer2", "Game2", null);
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
    @DisplayName("Test createGame success")
    public void testCreateGamePositive() throws DataAccessException {
        GameData gameData = new GameData(null, null, null, "Test Game", null);
        GameData game = gameService.createGame(gameData);
        assertNotNull(game);
    }

    @Test
    @DisplayName("Test createGame failure")
    public void testCreateGameNegative() throws DataAccessException {
        GameData gameData = new GameData(null, null, null, "Test Game", null);
        gameService.createGame(gameData);
        gameData = new GameData(2, null, null, "Test Game", null);

        GameData finalGameData = gameData;
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(finalGameData));
    }

    @Test
    @DisplayName("test joinGame success")
    public void testJoinGamePositive() throws DuplicateEntryException, BadRequestException, DataAccessException {
        GameData gameData = new GameData(null, null, null, "Test Game", null);
        gameData = gameDAO.createGame(gameData);
        gameService.joinGame(new UserData("testUser", "testPassword", "testEmail"), gameData.gameID(), ChessGame.TeamColor.WHITE);

        assertEquals("testUser", gameDAO.getGame(gameData.gameID()).whiteUsername());
    }

    @Test
    @DisplayName("test joinGame failure")
    public void testJoinGameNegative() throws DataAccessException {
        GameData gameData = new GameData(1, "WhitePlayer", null, "Test Game", null);
        gameDAO.createGame(gameData);

        assertThrows(DuplicateEntryException.class, () -> gameService.joinGame(new UserData("testUser", "testPassword", "testEmail"), 1, ChessGame.TeamColor.WHITE));
    }

    @Test
    public void testClear() {
        gameDAO.clear();
        assertTrue(gameDAO.listGames().isEmpty());
    }
}
