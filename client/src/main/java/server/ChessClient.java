package server;

import java.util.ArrayList;
import java.util.Arrays;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameBody;
import model.UserData;
import ui.ChessBoardUI;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private AuthData auth;
    private State state = State.SIGNEDOUT;
    private ArrayList<GameData> games = new ArrayList<>();

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
                case "observe" -> observeGame(params);
                case "leave" -> leaveGame();
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
        this.games = games;
        var result = new StringBuilder();
        int counter = 0;
        for (GameData game : games) {
            counter++;
            result.append(counter).append(". ").append(game.gameName()).append("\n - ").append(game.whiteUsername()).append("\n - ").append(game.blackUsername()).append("\n");
        }
        return result.toString();
    }


    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        GameData selectedGame;
        if (params.length >= 2) {
            int gameNumber = Integer.parseInt(params[0]);
            selectedGame = this.games.get(gameNumber - 1);
            var playerColor = params[1];
            JoinGameBody.Color color = null;
            if (playerColor.equals("white")){
                color = JoinGameBody.Color.WHITE;
            } else if (playerColor.equals("black")){
                color = JoinGameBody.Color.BLACK;
            }
            server.joinGame(this.auth, selectedGame.gameID(), color);
            state = State.PLAYING;
            ChessBoardUI ui = new ChessBoardUI(selectedGame.game());
            var prettyBoard = ui.prettyPrint();
            return String.format("%s", prettyBoard);
        }
        throw new ResponseException(400, "Expected: <game number> <color>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameName = params[0];
            var game = new GameData(null, null, null, gameName, new ChessGame());
            var newGame = server.createGame(this.auth, game);
//            this.games.add(newGame);
            return String.format("You created game \"%s\"", newGame.gameName());
        }
        throw new ResponseException(400, "Expected: <game name>");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameNumber = Integer.parseInt(params[0]);
            GameData selectedGame = this.games.get(gameNumber - 1);
            ChessBoardUI ui = new ChessBoardUI(selectedGame.game());
            state = State.PLAYING;
            return ui.prettyPrint();
        }
        throw new ResponseException(400, "Expected: <game number>");
    }

    public String leaveGame() {
        state = State.SIGNEDIN;
        return "You have left the game.";
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
                    - join <game number> <white|black>
                    - create <game name>
                    - observe <game number>
                    - signout
                    - help
                    - quit
                    """;

        }
        return """
                - game menu
                - leave
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