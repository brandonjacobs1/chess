package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.AuthData;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import server.ServerFacade;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private AuthData auth;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "register" -> register(params);
                case "signout" -> signOut();
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "create" -> createGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length >= 2) {
            var username = params[0];
            var password = params[1];
            AuthData auth = server.login(new UserData(username, password, null));
            state = State.SIGNEDIN;
            this.auth = auth;
            return String.format("You signed in as %s.", auth.username());
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData user = new UserData(username, password, email);
            AuthData auth  = server.register(user);
            state = State.SIGNEDIN;
            this.auth = auth;
            return String.format("You logged in as %s.", auth.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String signOut() throws ResponseException {
        assertSignedIn();
        server.logout(this.auth);
        this.auth = null;
        state = State.SIGNEDOUT;
        return String.format("You have signed out.");
    }
    public String listGames() throws ResponseException {
        assertSignedIn();
        var gameMap = this.server.listGames(this.auth);
        ArrayList<GameData> games = gameMap.get("games");
        var result = new StringBuilder();
        for (GameData game : games) {
            // Append the game name if the game map contains the "gameName" key
            result.append(game.gameID()).append(": ").append(game.gameName()).append("\n");
            }
        return result.toString();
    }


    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
        var gameID = Integer.parseInt(params[0]);
            var playerColor = params[1];
            JoinGameBody.Color color = null;
            if (playerColor.equals("white")){
                color = JoinGameBody.Color.WHITE;
            } else if (playerColor.equals("black")){
                color = JoinGameBody.Color.BLACK;
            }
            server.joinGame(this.auth, gameID, color);
            state = State.PLAYING;
            return String.format("You joined game %d.", gameID);
        }
        throw new ResponseException(400, "Expected: <game id> <color>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameName = params[0];
            var game = new GameData(null, null, null, gameName, new ChessGame());
            var newGame = server.createGame(this.auth, game);
            return String.format("You created game %d: %s", newGame.gameID(), newGame.gameName());
        }
        throw new ResponseException(400, "Expected: <game name>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signin <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
        } else if (state == State.SIGNEDIN) {
            return """
                    - list
                    - join <game id> <white|black|either>
                    - create <game name>
                    - signout
                    - help
                    - quit
                    """;

        }
        return """
                - game stuff
                - help
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}