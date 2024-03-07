package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SqlAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private SQLGameDAO gameDAO;

    @BeforeEach
    public void setUp() {
        gameDAO = SQLGameDAO.getInstance();
    }

    @Test
    public void testCreateGame_Positive() {
        GameData game = new GameData(null, "white", "black", "Chess", new ChessGame());
        try {
            GameData createdGame = gameDAO.createGame(game);
            assertNotNull(createdGame);
            assertEquals(game.whiteUsername(), createdGame.whiteUsername());
            assertEquals(game.blackUsername(), createdGame.blackUsername());
            assertEquals(game.gameName(), createdGame.gameName());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateGame_Negative() {
        // The only way to negatively test this method is if there is a DB connection error.
        // This is because the fields are all auto incremented or nullable.
        GameData game = new GameData(null, null, "black", "Chess", new ChessGame());
        assertDoesNotThrow(() -> {
            gameDAO.createGame(game);
        });
    }

    @Test
    public void testUpdateGame_Positive() {
        GameData game = new GameData(1, "white", "black", "Chess", new ChessGame());
        GameData createdGame = null;
        try {
            createdGame = gameDAO.createGame(game);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData finalCreatedGame = createdGame;
        assertDoesNotThrow(() -> gameDAO.updateGame(new GameData(finalCreatedGame.gameID(), "white", "black", "Updated Chess", new ChessGame())));

        try {
            GameData updatedGame = gameDAO.getGame(finalCreatedGame.gameID());
            assertEquals("Updated Chess", updatedGame.gameName());
        } catch (BadRequestException | DataAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUpdateGame_Negative() {
        GameData game = new GameData(1000, "white", "black", "Chess", new ChessGame());
        assertThrows(BadRequestException.class, () -> gameDAO.updateGame(game));
    }

    @Test
    public void testListGames_Positive() {
        assertDoesNotThrow(() -> {
            gameDAO.listGames();
        });
    }

    @Test
    public void testGetGame_Positive() {
        GameData game = new GameData(1, "white", "black", "Chess", new ChessGame());
        GameData createdGame = new GameData(null, null, null, null, new ChessGame());
        try {
            createdGame = gameDAO.createGame(game);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData finalCreatedGame = createdGame;
        assertDoesNotThrow(() -> {
            GameData retrievedGame = gameDAO.getGame(finalCreatedGame.gameID());
            assertNotNull(retrievedGame);
            assertEquals(finalCreatedGame.gameID(), retrievedGame.gameID());
        });
    }

    @Test
    public void testGetGame_Negative() {
        assertThrows(BadRequestException.class, () -> gameDAO.getGame(1000));
    }

    @Test
    public void testClear_Positive() {
        assertDoesNotThrow(() -> gameDAO.clear());
    }
}
