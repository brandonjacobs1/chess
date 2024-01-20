package chess;

import java.util.ArrayList;

public class MoveCalculator {
    
    public MoveCalculator () {}
    
    public static ArrayList<ChessMove> calculateKingKnightMoves(ArrayList<ChessPosition> coordinateChanges, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
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

    private static void checkLine(int startRow, int startCol, ArrayList<ChessMove> moves, ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor startColor, int changeX, int changeY) {

        int endRow = startRow + changeX;
        int endCol = startCol + changeY;

        while (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) { // Checks that move is in bounds
            ChessPiece endPiece = board.getPiece(new ChessPosition(endRow, endCol));

            if (endPiece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
            } else if (endPiece.teamColor == startColor) {
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

    private static void checkDiagonal(int startRow, int startCol, ArrayList<ChessMove> moves, ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor startColor, int changeX, int changeY) {
        int endRow = startRow + changeX;
        int endCol = startCol + changeY;

        while (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) { // Checks that move is in bounds
            ChessPiece endPiece = board.getPiece(new ChessPosition(endRow, endCol));

            if (endPiece == null) {
                moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
            } else if (endPiece.teamColor == startColor) {
                break;
            } else {
                moves.add(new ChessMove(startPosition, new ChessPosition(endRow, endCol), null));
                break;
            }

            // Update the next position along the diagonal
            endRow += changeX;
            endCol += changeY;
        }
    }



    public static ArrayList<ChessMove> calculateBishopMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        checkDiagonal(startRow, startCol, moves, board, startPosition, startColor, 1, 1);
        checkDiagonal(startRow, startCol, moves, board, startPosition, startColor, -1, 1);
        checkDiagonal(startRow, startCol, moves, board, startPosition, startColor, -1, 1);
        checkDiagonal(startRow, startCol, moves, board, startPosition, startColor, -1, -1);

        return moves;
    }
    public static ArrayList<ChessMove> calculateRookMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, -1);

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

        positions.add(new ChessPosition(0, 1));
        positions.add(new ChessPosition(1, 1));
        positions.add(new ChessPosition(1, 0));
        positions.add(new ChessPosition(1, -1));
        positions.add(new ChessPosition(0, -1));
        positions.add(new ChessPosition(-1, -1));
        positions.add(new ChessPosition(-1, 0));
        positions.add(new ChessPosition(-1, 1));

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
