package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.ChessBoardUI;
import webSocket.ServerMessageHandler;
import webSocket.WebSocketFacade;

import static java.lang.Integer.parseInt;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private AuthData auth;
    private State state = State.SIGNEDOUT;
    private ArrayList<GameData> games = new ArrayList<>();
    private ServerMessageHandler serverMessageHandler;
    private WebSocketFacade ws;
    private GameData game;


    public ChessClient(String serverUrl, ServerMessageHandler serverMessageHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.serverMessageHandler = serverMessageHandler;
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
                case "show" -> showMoves(params);
                case "move" -> makeMove(params);
                case "redraw" -> redraw();
                case "resign" -> resign();
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
    private void setGames() throws ResponseException {
        try {
            var gameMap = this.server.listGames(this.auth);
            ArrayList<GameData> games = gameMap.get("games");
            this.games = games;
        } catch (Exception e){
           throw new ResponseException(500, "Error getting games");
        }
    }
    public String listGames() throws ResponseException {
        assertSignedIn();
        setGames();
        var result = new StringBuilder();
        int counter = 0;
        if (games.isEmpty()) {
            return "No games available. To create a game tyoe \"create <game name>\"";
        }
        for (GameData game : games) {
            counter++;
            var whiteText = new StringBuilder();
            var blackText = new StringBuilder();

            if (game.whiteUsername() == null) {
                whiteText.append("Type \"join ").append(counter).append(" white\" to join game");
            } else {
                whiteText.append(game.whiteUsername());
            }
            if (game.blackUsername() == null) {
                blackText.append("Type \"join ").append(counter).append(" black\" to join game");
            } else {
                blackText.append(game.blackUsername());
            }
            result.append(counter).append(". ").append(game.gameName()).append("\n white: ").append(whiteText).append("\n black: ").append(blackText).append("\n");
        }
        return result.toString();
    }


    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        GameData selectedGame;
        if (params.length >= 2) {
            int gameNumber = parseInt(params[0]);
            selectedGame = this.games.get(gameNumber - 1);
            var playerColor = params[1];
            ChessGame.TeamColor color = null;
            if (playerColor.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (playerColor.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            }
            server.joinGame(this.auth, selectedGame.gameID(), color);
            setGames();
            selectedGame = this.games.get(gameNumber - 1);
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.joinPlayer(this.auth.authToken(),selectedGame.gameID(), color);
            state = State.PLAYING;
            game = selectedGame;
            return String.format("You joined game \"%s\" as %s!", selectedGame.gameName(), playerColor);
            }
        throw new ResponseException(400, "Expected: <game number> <color>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameName = params[0];
            var game = new GameData(null, null, null, gameName, new ChessGame());
            var newGame = server.createGame(this.auth, game);
            return String.format("You created game \"%s\"", newGame.gameName());
        }
        throw new ResponseException(400, "Expected: <game name>");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameNumber = parseInt(params[0]);
            GameData selectedGame = this.games.get(gameNumber - 1);
            if (selectedGame.game() == null) {
                throw new ResponseException(400, "Game has not been started. Please wait for a player to join.");
            }
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.joinObserver(this.auth.authToken(),selectedGame.gameID());
            state = State.PLAYING;
            game = selectedGame;
            return String.format("You joined game \"%s\" as an observer!", selectedGame.gameName());
        }
        throw new ResponseException(400, "Expected: <game number>");
    }

    public String leaveGame() {
        state = State.SIGNEDIN;
        game = null;
        return "You have left the game.";
    }

    public String showMoves(String... params) throws ResponseException {
        if (params.length >= 1) {
            var positionString = params[0];
            ChessPosition position = parsePosition(positionString);
            Collection<ChessMove> validMoves = game.game().validMoves(position);
            String username = auth.username();
            ChessBoardUI ui = new ChessBoardUI(game.game());
            return ui.toString(username, game.blackUsername(), game.whiteUsername(), validMoves);
        }
        throw new ResponseException(400, "Expected: <position>");

    }

    private ChessPosition parsePosition(String position) throws ResponseException {
        int col;
        int row;
        try {
            char letter = position.charAt(0);
            row = parseInt(String.valueOf(position.charAt(1)));
            switch (letter) {
                case 'a' -> col = 1;
                case 'b' -> col = 2;
                case 'c' -> col = 3;
                case 'd' -> col = 4;
                case 'e' -> col = 5;
                case 'f' -> col = 6;
                case 'g' -> col = 7;
                case 'h' -> col = 8;
                default -> throw new ResponseException(400, "Invalid position");
            };
        } catch (Exception e) {
            throw new ResponseException(400, "Invalid position");
        }
        return new ChessPosition(row, col);
    }

    public String makeMove(String... params) throws ResponseException {
        if (params.length >= 2) {
            var from = params[0];
            var to = params[1];
            return "Move made";
        }
        throw new ResponseException(400, "Expected: <from> <to>");
    }

    public String redraw() {
        String username = auth.username();
        ChessBoardUI ui = new ChessBoardUI(game.game());
        return ui.toString(username, game.blackUsername(), game.whiteUsername(), null);
    }

    public String resign() {
        return "You have resigned";
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
                - show <position>
                    - This command will show available moves.Position should be row number then column letter.
                - move <from> <to>
                    - From and to represent a piece position. Should be row number then column letter.
                - redraw
                - resign
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