package server.Handler;

import dataAccess.DataAccessException;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import server.BadRequestException;
import server.DuplicateEntryException;
import spark.Request;
import spark.Response;

import java.util.*;

public class GameHandler extends APIHandler{
    public Object createGameHandler(Request req, Response res) throws BadRequestException, DataAccessException {
        try {
            // validate body
            GameData game;
            try {
                game = serializer.fromJson(req.body(), GameData.class);
            } catch (Exception e) {
                throw new BadRequestException("There was an error in your JSON body");
            }
            List<String> keysToValidate = List.of("gameName");
            validateBody(game, keysToValidate);
            // send to service
            GameData newGame = gameService.createGame(game);
            // return a response
            return serializer.toJson(newGame);
        } catch (DataAccessException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    public Object listGameHandler(Request req, Response res) throws DataAccessException {
        // send to service
        ArrayList<GameData> games = gameService.listGames();
        Map<String, ArrayList<GameData>> responseMap = new HashMap<>();
        responseMap.put("games", games);

        // return a response
        return serializer.toJson(responseMap);
    }
    public Object joinGameHandler(Request req, Response res) throws BadRequestException, DataAccessException, DuplicateEntryException {
        try {
            // validate body
            JoinGameBody body;
            try {
                body = serializer.fromJson(req.body(), JoinGameBody.class);
            } catch (Exception e) {
                throw new BadRequestException("There was an error in your JSON body");
            }
            List<String> keysToValidate = List.of("gameID");
            validateBody(body, keysToValidate);
            // send to service
            UserData user = userService.getUser(req.headers("authorization"));
            gameService.joinGame(user, body.gameID(), body.playerColor());
            // return a response
            return "{}";
        } catch (DataAccessException | BadRequestException | DuplicateEntryException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
