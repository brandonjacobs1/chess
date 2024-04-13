package webSocket;

import chess.ChessGame;
import model.GameData;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void showNotificationMessage(NotificationMessage notification);

    void showErrorMessage(ErrorMessage message);

    void showLoadGameMessage(String s);

    void refreshGameList(GameData game);
}