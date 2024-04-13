package ui;

import chess.*;

import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    ChessGame game;
    private Collection<ChessMove> validMoves;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BROWN_BACKGROUND = "\u001B[48;5;222m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[48;5;95m";

    public ChessBoardUI(ChessGame game) {
        this.game = game;
    }

    public String toString(String username, String blackUsername, String whiteUsername, Collection<ChessMove> validMoves) {
        this.validMoves = validMoves;
        if (Objects.equals(username, whiteUsername)) {
            return printWhite();
        } else if (Objects.equals(username, blackUsername)) {
            return printBlack();
        } else {
            return printObserver();
        }

    }

    public String prettyPrint() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoardReversed(board, result);
        result.append("\n\n");
        printBoard(board, result);
        return result.toString();
    }

    public String printBlack() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoardReversed(board, result);
        return result.toString();
    }

    public String printWhite() {
        StringBuilder result = new StringBuilder();
        ChessBoard board = game.getBoard();
        printBoard(board, result);
        return result.toString();
    }

    public String printObserver() {
        return prettyPrint();
    }

    private void printBoard(ChessBoard chessBoard, StringBuilder result) {
        ChessPiece[][] board = chessBoard.getBoard();
        result.append("\n");
        // Print top row letters
        result.append(ANSI_RESET).append("   "); // Add leading spaces for alignment
        aToH(result, chessBoard);
        result.append("\n");

        for (int row = 8; row > 0; row--) {
            // Print left column numbers
            result.append(ANSI_RESET).append(row).append(" ");
            for (int col = 1; col <= 8; col++) {
                addSquareAndPiece(result, chessBoard, new ChessPosition(row, col));
            }
            // Print right column numbers
            result.append(" ").append(row).append("\n");
        }

        // Print bottom row letters
        result.append("   "); // Add leading spaces for alignment
        aToH(result, chessBoard);
    }

    private void addSquareAndPiece(StringBuilder result, ChessBoard board, ChessPosition position) {
        String backgroundColor = (position.getRow() + position.getColumn()) % 2 == 0 ?  ANSI_YELLOW_BACKGROUND : ANSI_BROWN_BACKGROUND;
        String highlightColor = SET_BG_COLOR_BLUE;
        boolean showValidMove = false;
        boolean showBlinking = false;
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                if (move.getEndPosition().equals(position)) {
                    showValidMove = true;
                    break;
                } else if (move.getStartPosition().equals(position)) {
                    showBlinking = true;
                    break;
                }
            }
        }
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            if (showValidMove) {
                // Highlighted square
                result.append(ANSI_RESET).append(highlightColor).append("   ").append(ANSI_RESET);
            } else {
                // no piece on square
                result.append(ANSI_RESET).append(backgroundColor).append("   ").append(ANSI_RESET);
            }
        } else if (showBlinking) {
            // Blinking piece
            String symbol = getPieceSymbol(piece);
            result.append(ANSI_RESET).append(backgroundColor).append(SET_TEXT_BOLD).append(SET_TEXT_COLOR_BLUE).append(symbol).append(ANSI_RESET);
        } else {
            String symbol = getPieceSymbol(piece);
            if (showValidMove) {
                // Highlighted square
                result.append(ANSI_RESET).append(highlightColor).append(SET_TEXT_BOLD).append(BLACK).append(symbol).append(ANSI_RESET);
            } else {
                // Normal piece
                result.append(ANSI_RESET).append(backgroundColor).append(SET_TEXT_BOLD).append(BLACK).append(symbol).append(ANSI_RESET);
            }
        }
    }

    private static void aToH(StringBuilder result, ChessBoard chessBoard) {
        ChessPiece[][] board = chessBoard.getBoard();
        for (int row = 0; row < board.length; row++) {
            result.append((char) ('a' + row)).append("  ");
        }
    }

    private void printBoardReversed(ChessBoard chessBoard, StringBuilder result) {
        ChessPiece[][] board = chessBoard.getBoard();
        result.append("\n");
        // Print bottom row letters
        result.append(ANSI_RESET).append("   "); // Add leading spaces for alignment
        hToA(result, chessBoard);
        result.append("\n");

        for (int row = 1; row <= 8; row++) {
            // Print left column numbers
            result.append(ANSI_RESET).append(row).append(" ");
            for (int col = 8; col > 0; col--) {
                addSquareAndPiece(result, chessBoard, new ChessPosition(row, col));
            }
            // Print right column numbers
            result.append(" ").append(row).append("\n");
        }

        // Print top row letters
        result.append("   "); // Add leading spaces for alignment
        hToA(result, chessBoard);
    }

    private static void hToA(StringBuilder result, ChessBoard chessBoard) {
        ChessPiece[][] board = chessBoard.getBoard();
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
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
