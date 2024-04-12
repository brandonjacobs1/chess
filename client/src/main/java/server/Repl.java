package server;

import webSocket.ServerMessageHandler;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to chess! Sign in or register to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void showLoadGameMessage(String board) {
        System.out.println(board);
        printPrompt();
    }

    public void showNotificationMessage(NotificationMessage notification) {
        StringBuilder sb = new StringBuilder();
        sb.append(ERASE_LINE).append(RED).append(notification.getMessage()).append(RESET);
        System.out.print(sb);
        printPrompt();
    }
    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}