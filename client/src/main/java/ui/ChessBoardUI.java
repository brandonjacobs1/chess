package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static chess.ChessPiece.PieceType.*;

public class ChessBoardUI {
    ChessGame game;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

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

    private void printBoard(ChessBoard fullBoard, StringBuilder result) {
        ChessPiece[][] board = fullBoard.getBoard();
        // Print top row letters
        result.append(ANSI_RESET).append("   "); // Add leading spaces for alignment
        for (int row = 0; row < board.length; row++) {
            result.append((char) ('a' + row)).append("  ");
        }
        result.append("\n");

        for (int col = 0; col < board[0].length; col++) {
            // Print left column numbers
            result.append(col + 1).append(" ");
            for (int row = 0; row < board.length; row++) {
                String backgroundColor = (row + col) % 2 == 0 ? ANSI_WHITE_BACKGROUND : ANSI_YELLOW_BACKGROUND;
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    result.append(ANSI_RESET).append(backgroundColor).append("   ").append(ANSI_RESET); // Adjust spaces as needed
                } else {
                    String symbol = getPieceSymbol(piece);
                    result.append(ANSI_RESET).append(backgroundColor).append(symbol).append(ANSI_RESET);
                }
            }
            // Print right column numbers
            result.append(" ").append(col + 1).append("\n");
        }

        // Print bottom row letters
        result.append("   "); // Add leading spaces for alignment
        for (int row = 0; row < board.length; row++) {
            result.append((char) ('a' + row)).append("  ");
        }
    }

    private void printBoardReversed(ChessBoard fullBoard, StringBuilder result) {
        ChessPiece[][] board = fullBoard.getBoard();

        // Print bottom row letters
        result.append("   "); // Add leading spaces for alignment
        for (int row = board.length - 1; row >= 0; row--) {
            result.append((char) ('a' + row)).append("  ");
        }
        result.append("\n");

        for (int col = board[0].length - 1; col >= 0; col--) {
            // Print left column numbers
            result.append(col + 1).append(" ");
            for (int row = board.length - 1; row >= 0; row--) {
                String backgroundColor = (row + col) % 2 == 0 ? ANSI_WHITE_BACKGROUND : ANSI_YELLOW_BACKGROUND;
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    result.append(ANSI_RESET).append(backgroundColor).append("   ").append(ANSI_RESET); // Adjust spaces as needed
                } else {
                    String symbol = getPieceSymbol(piece);
                    result.append(ANSI_RESET).append(backgroundColor).append(symbol).append(ANSI_RESET);
                }
            }
            // Print right column numbers
            result.append(" ").append(col + 1).append("\n");
        }

        // Print top row letters
        result.append("   "); // Add leading spaces for alignment
        for (int row = board.length - 1; row >= 0; row--) {
            result.append((char) ('a' + row)).append("  ");
        }
    }




    private String getPieceSymbol(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case KING:
                    return EscapeSequences.WHITE_KING;
                case QUEEN:
                    return EscapeSequences.WHITE_QUEEN;
                case BISHOP:
                    return EscapeSequences.WHITE_BISHOP;
                case KNIGHT:
                    return EscapeSequences.WHITE_KNIGHT;
                case ROOK:
                    return EscapeSequences.WHITE_ROOK;
                case PAWN:
                    return EscapeSequences.WHITE_PAWN;
                default:
                    return EscapeSequences.EMPTY;
            }
        } else {
            switch (piece.getPieceType()) {
                case KING:
                    return EscapeSequences.BLACK_KING;
                case QUEEN:
                    return EscapeSequences.BLACK_QUEEN;
                case BISHOP:
                    return EscapeSequences.BLACK_BISHOP;
                case KNIGHT:
                    return EscapeSequences.BLACK_KNIGHT;
                case ROOK:
                    return EscapeSequences.BLACK_ROOK;
                case PAWN:
                    return EscapeSequences.BLACK_PAWN;
                default:
                    return EscapeSequences.EMPTY;
            }
        }
    }
}
