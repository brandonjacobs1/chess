package server.Handler;

import dataAccess.DataAccessException;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import server.BadRequestException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            List<String> keysToValidate = Arrays.asList("gameName");
            validateBody(game, keysToValidate);
            // send to service
            UserData user = userService.getUser(req.headers("authorization"));
            GameData newGame = gameService.createGame(game, user);
            // return a response
            return serializer.toJson(newGame);
        } catch (DataAccessException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    public Object listGameHandler(Request req, Response res) throws DataAccessException {
        try {
            // send to service
            ArrayList<GameData> games = gameService.listGames();
            // return a response
            return serializer.toJson(games);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
    public Object joinGameHandler(Request req, Response res) throws BadRequestException, DataAccessException {
        try {
            // validate body
            JoinGameBody body;
            try {
                body = serializer.fromJson(req.body(), JoinGameBody.class);
            } catch (Exception e) {
                throw new BadRequestException("There was an error in your JSON body");
            }
            List<String> keysToValidate = Arrays.asList("playerColor", "gameID");
            validateBody(body, keysToValidate);
            // send to service
            UserData user = userService.getUser(req.headers("authorization"));
            gameService.joinGame(user, body.gameID(), body.playerColor());
            // return a response
            return "{}";
        } catch (DataAccessException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
