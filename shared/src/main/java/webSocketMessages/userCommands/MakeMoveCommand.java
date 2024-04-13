package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameId;
    private final ChessMove move;


    public MakeMoveCommand(String authToken, ChessMove move, int gameId) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameId = gameId;
        this.move = move;

    }

    public int getGameId() {
        return gameId;
    }

    public ChessMove getMove() {
        return move;
    }

    public String message() {
        return move.toString();
    }
}
