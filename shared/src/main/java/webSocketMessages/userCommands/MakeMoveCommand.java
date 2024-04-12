package webSocketMessages.userCommands;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameId;
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;

    public MakeMoveCommand(String authToken, int gameId, int fromRow, int fromCol, int toRow, int toCol) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameId = gameId;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public int getGameId() {
        return gameId;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public String message() {
        return "Making move from " + fromRow + ", " + fromCol + " to " + toRow + ", " + toCol;
    }
}
