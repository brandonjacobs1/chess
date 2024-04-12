package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameId;

    public LeaveCommand(String authToken, int gameId) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
