package chess.MoveCalculators;

import chess.*;

import java.util.ArrayList;

public class SharedMoves {
    private static void checkLine(int startRow, int startCol, ArrayList<ChessMove> moves, ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor startColor, int changeX, int changeY) {

        int endRow = startRow + changeX;
        int endCol = startCol + changeY;

        while (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) { // Checks that move is in bounds
            ChessPiece endPiece = board.getPiece(new ChessPosition(endRow, endCol));

            if (endPiece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
            } else if (endPiece.getTeamColor() == startColor) {
                break;
            } else {
                moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
                break;
            }

            // Update the next position along the line
            endRow += changeX;
            endCol += changeY;
        }
    }

    static ArrayList<ChessMove> checkAbsolutePositions(ArrayList<ChessPosition> coordinateChanges, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        for (ChessPosition c : coordinateChanges) {
            int endRow = c.getRow() + startRow;
            int endCol = c.getColumn() + startCol;

            if (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) { // Checks that move is in bounds
                ChessPiece endPiece = board.getPiece(new ChessPosition(endRow, endCol));
                if (endPiece == null) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
                } else if (endPiece.getTeamColor() != startColor) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
                }
            }
        }
        return moves;
    }

    public static void straightLineMove (ArrayList<ChessMove> moves, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor, int startRow, int startCol) {
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, -1);
    }

    public static void diagonalLineMove (ArrayList<ChessMove> moves, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor, int startRow, int startCol) {
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, -1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, -1);
    }
}
