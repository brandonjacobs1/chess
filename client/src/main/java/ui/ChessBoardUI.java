package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    ChessGame game;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BROWN_BACKGROUND = "\u001B[48;5;222m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[48;5;95m";

    public ChessBoardUI(ChessGame game) {
        this.game = game;
    }

    public String prettyPrint() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoard(board, result);
        result.append("\n\n");
        printBoardReversed(board, result);
        return result.toString();
    }

    public String printWhite() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoard(board, result);
        return result.toString();
    }

    public String printBlack() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoardReversed(board, result);
        return result.toString();
    }

    public String printObserver() {
        return printWhite();
    }

    private void printBoard(ChessBoard fullBoard, StringBuilder result) {
        ChessPiece[][] board = fullBoard.getBoard();
        result.append("\n");
        // Print top row letters
        result.append(ANSI_RESET).append("   "); // Add leading spaces for alignment
        hToA(result, board);
        result.append("\n");

        for (int col = 0; col < board[0].length; col++) {
            // Print left column numbers
            result.append(ANSI_RESET).append(col + 1).append(" ");
            for (int row = 0; row < board.length; row++) {
                addSquareAndPiece(result, board, col, row);
            }
            // Print right column numbers
            result.append(" ").append(col + 1).append("\n");
        }

        // Print bottom row letters
        result.append("   "); // Add leading spaces for alignment
        hToA(result, board);
    }

    private void addSquareAndPiece(StringBuilder result, ChessPiece[][] board, int col, int row) {
        String backgroundColor = (row + col) % 2 == 0 ? ANSI_BROWN_BACKGROUND : ANSI_YELLOW_BACKGROUND;
        ChessPiece piece = board[row][col];
        if (piece == null) {
            result.append(ANSI_RESET).append(backgroundColor).append("   ").append(ANSI_RESET);
        } else {
            String symbol = getPieceSymbol(piece);
            result.append(ANSI_RESET).append(backgroundColor).append(SET_TEXT_BOLD).append(BLACK).append(symbol).append(ANSI_RESET);
        }
    }

    private static void aToH(StringBuilder result, ChessPiece[][] board) {
        for (int row = 0; row < board.length; row++) {
            result.append((char) ('a' + row)).append("  ");
        }
    }

    private void printBoardReversed(ChessBoard fullBoard, StringBuilder result) {
        ChessPiece[][] board = fullBoard.getBoard();

        result.append("\n");
        // Print bottom row letters
        result.append(ANSI_RESET).append("   "); // Add leading spaces for alignment
        aToH(result, board);
        result.append("\n");

        for (int col = board[0].length - 1; col >= 0; col--) {
            // Print left column numbers
            result.append(col + 1).append(" ");
            for (int row = board.length - 1; row >= 0; row--) {
                addSquareAndPiece(result, board, col, row);
            }
            // Print right column numbers
            result.append(" ").append(col + 1).append("\n");
        }

        // Print top row letters
        result.append("   "); // Add leading spaces for alignment
        aToH(result, board);
    }

    private static void hToA(StringBuilder result, ChessPiece[][] board) {
        for (int row = board.length - 1; row >= 0; row--) {
            result.append((char) ('a' + row)).append("  ");
        }
    }


    private String getPieceSymbol(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
                default -> EscapeSequences.EMPTY;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
                default -> EscapeSequences.EMPTY;
            };
        }
    }
}
