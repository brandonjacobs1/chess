package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static chess.ChessPiece.PieceType.*;

public class ChessBoardUI {
    ChessGame game;

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
        for (int col = 0; col < board[0].length; col++) {
            result.append("\n");
            for (int row = 0; row < board.length; row++) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    result.append(EscapeSequences.EMPTY);
                } else {
                    String symbol = getPieceSymbol(piece);
                    result.append(symbol);
                }
            }
        }
    }

    private void printBoardReversed(ChessBoard fullBoard, StringBuilder result) {
        ChessPiece[][] board = fullBoard.getBoard();
        for (int col = board[0].length - 1; col >= 0; col--) {
            result.append("\n");
            for (int row = board.length - 1; row >= 0; row--) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    result.append(EscapeSequences.EMPTY);
                } else {
                    String symbol = getPieceSymbol(piece);
                    result.append(symbol);
                }
            }
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
