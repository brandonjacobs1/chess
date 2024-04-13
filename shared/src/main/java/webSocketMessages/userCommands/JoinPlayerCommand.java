package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    public  int gameID;
    public ChessGame.TeamColor playerColor;
    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public int getGameID() {
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
