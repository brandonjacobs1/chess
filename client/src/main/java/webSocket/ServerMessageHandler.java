package webSocket;

import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void showMessage(ServerMessage notification);
}