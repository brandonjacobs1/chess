package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    private final int gameId;
    private final ChessGame.TeamColor teamColor;
    public JoinPlayerCommand(String authToken, int gameId, ChessGame.TeamColor teamColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameId = gameId;
        this.teamColor = teamColor;
    }
    public int getGameId() {
        return gameId;
    }
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
