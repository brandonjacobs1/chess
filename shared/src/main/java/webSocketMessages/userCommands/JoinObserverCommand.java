package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public int gameID;

    public JoinObserverCommand(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
