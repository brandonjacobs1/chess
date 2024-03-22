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
//                case "create" -> createGame(params);
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
            return String.format("You joined game %d.", gameID);
        }
        throw new ResponseException(400, "Expected: <game id> <color>");
    }
//
//    public String adoptAllPets() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (var pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
//    }

//    private Pet getPet(int id) throws ResponseException {
//        for (var pet : server.listPets()) {
//            if (pet.id() == id) {
//                return pet;
//            }
//        }
//        return null;
//    }

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
                    - create
                    - signout
                    - quit
                    """;

        }
        return """
                - game stuff
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}