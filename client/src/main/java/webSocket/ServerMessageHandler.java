package webSocket;

import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void showNotificationMessage(NotificationMessage notification);

    void showLoadGameMessage(String s);
}