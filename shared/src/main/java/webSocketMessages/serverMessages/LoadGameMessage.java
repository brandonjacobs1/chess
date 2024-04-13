package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private final GameData game;
    private final String username;

    public LoadGameMessage(GameData game, String username) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.username = username;
    }

    public GameData getGame() {
        return game;
    }

    public String getUsername() {
        return username;
    }
}
