package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    public int gameID;

    public LeaveCommand(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
