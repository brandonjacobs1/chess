package chess;

import java.util.ArrayList;

public class MoveCalculator {
    
    public MoveCalculator () {}
    
    public static ArrayList<ChessMove> calculateMoves(ArrayList<ChessPosition> coordinateChanges, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
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
                } else if (endPiece.teamColor != startColor) {
                    moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
                }
            }
        }
        return moves;
    }

    public static ArrayList<ChessPosition> rookCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            positions.add(new ChessPosition(0, i));
            positions.add(new ChessPosition(0, i * -1));
            positions.add(new ChessPosition(i, 0));
            positions.add(new ChessPosition(i * -1, 0));
        }
        return positions;
    }
    public static ArrayList<ChessPosition> knightCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(1, 2));
        positions.add(new ChessPosition(2, 1));
        positions.add(new ChessPosition(2, -1));
        positions.add(new ChessPosition(1, -2));
        positions.add(new ChessPosition(-1, -2));
        positions.add(new ChessPosition(-2, -1));
        positions.add(new ChessPosition(-2, 1));
        positions.add(new ChessPosition(-1, 2));

        return positions;
    }

    public static ArrayList<ChessPosition> bishopCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        return positions;

    }
    public static ArrayList<ChessPosition> kingCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        return positions;

    }
    public static ArrayList<ChessPosition> queenCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        return positions;

    }
    public static ArrayList<ChessPosition> pawnCoordinateChanges() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        return positions;

    }

}