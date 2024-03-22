import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import server.Repl;
import ui.ChessBoardUI;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
//        ChessGame game = new ChessGame();
//        game.setBoard(new ChessBoard());
//        game.getBoard().resetBoard();
//        ChessBoardUI ui = new ChessBoardUI(game);
//        var x = ui.prettyPrint();
//        System.out.println(x);
    }
}
