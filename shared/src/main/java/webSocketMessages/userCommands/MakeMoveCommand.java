package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    public int gameID;
    public ChessMove move;


    public MakeMoveCommand(String authToken, ChessMove move, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;

    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public String message() {
        return move.toString();
    }
}
