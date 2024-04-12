package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    private final int gameId;

    public JoinObserverCommand(String authToken, int gameId) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
