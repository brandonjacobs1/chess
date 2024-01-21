package chess;

import java.util.ArrayList;

public class MoveCalculator {
    
    public MoveCalculator () {}
    
    public static ArrayList<ChessMove> checkAbsolutePositions(ArrayList<ChessPosition> coordinateChanges, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
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

    public static ArrayList<ChessMove> calculateBishopMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, -1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, -1);

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


    public static ArrayList<ChessMove> calculateKnightMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(1, 2));
        positions.add(new ChessPosition(2, 1));
        positions.add(new ChessPosition(2, -1));
        positions.add(new ChessPosition(1, -2));
        positions.add(new ChessPosition(-1, -2));
        positions.add(new ChessPosition(-2, -1));
        positions.add(new ChessPosition(-2, 1));
        positions.add(new ChessPosition(-1, 2));

        return checkAbsolutePositions(positions, startPosition, board, startColor);
    }
    public static ArrayList<ChessMove> calculateKingMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(0, 1));
        positions.add(new ChessPosition(1, 1));
        positions.add(new ChessPosition(1, 0));
        positions.add(new ChessPosition(1, -1));
        positions.add(new ChessPosition(0, -1));
        positions.add(new ChessPosition(-1, -1));
        positions.add(new ChessPosition(-1, 0));
        positions.add(new ChessPosition(-1, 1));

        return checkAbsolutePositions(positions, startPosition, board, startColor);

    }

    public static ArrayList<ChessMove> calculateQueenMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        //Check vertical/horizontal
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 0);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 0, -1);
        // Check diagonal
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, 1, -1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, 1);
        checkLine(startRow, startCol, moves, board, startPosition, startColor, -1, -1);

        return moves;
    }
    public static ArrayList<ChessMove> calculatePawnMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {

        int moveMultiplier = (ChessGame.TeamColor.BLACK == startColor) ? -1 : 1;
        ArrayList<ChessPosition>  positions = new ArrayList<>();

        positions.add(new ChessPosition(moveMultiplier, 0));
        positions.add(new ChessPosition(2 * moveMultiplier, 0));
        positions.add(new ChessPosition(moveMultiplier, -1 * moveMultiplier));
        positions.add(new ChessPosition(-1 * moveMultiplier, moveMultiplier));

        return checkPawnMoves(positions, startPosition, board, startColor);
    }

    private static boolean isDiagonal(ChessPosition start, ChessPosition end) {
        int rowDifference = Math.abs(start.getRow() - end.getRow());
        int colDifference = Math.abs(start.getColumn() - end.getColumn());

        return rowDifference == colDifference;
    }
    private static boolean isOpposingTeam(ChessGame.TeamColor startColor, ChessGame.TeamColor opposingColor){
        return startColor != opposingColor;
    }

    private static boolean isFirstMove(ChessPosition start, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE && start.getRow() == 2) {
            return true;
        } else if (color == ChessGame.TeamColor.BLACK && start.getRow() == 7) {
            return true;
        }
        return false;
    }

    private static boolean is2SpaceMove(ChessPosition start, ChessPosition end) {
        return start.getRow() - end.getRow() == 2 || start.getRow() - end.getRow() == -2;
    }

    private static boolean isValidSpace(ChessPosition start, ChessPosition end, ChessPiece newSpace, ChessGame.TeamColor startColor, ChessBoard board) {
        boolean isDiagonal = isDiagonal(start, end);
        boolean isFirstMove = isFirstMove(start, startColor);
        boolean is2SpaceMove = is2SpaceMove(start, end);

        if (newSpace == null) {
            if (!isDiagonal) {
                if (is2SpaceMove) {
                    int moveMultiplier = (ChessGame.TeamColor.BLACK == startColor) ? -1 : 1;
                    ChessPiece piece = board.getPiece(new ChessPosition(start.getRow() + (moveMultiplier), start.getColumn()));
                    return isFirstMove && piece == null;
                } else {
                    return true;
                }
            }
        } else {
            boolean isOpposingTeam = isOpposingTeam(startColor, newSpace.teamColor);
            if (isDiagonal && isOpposingTeam) {
                return true;
            }
        }

        return false;
    }


    public static ArrayList<ChessMove> checkPawnMoves (ArrayList<ChessPosition> positions, ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        for (ChessPosition p : positions) {
            int endRow = startPosition.getRow() + p.getRow();
            int endCol = startPosition.getColumn() + p.getColumn();

            if (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) { // Checks that move is in bounds
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                boolean isValid = isValidSpace(startPosition, endPosition, board.getPiece(endPosition), startColor, board);
                if (isValid) {
                    moves.add(new ChessMove(startPosition, endPosition, null));
                }
            }
        }
        return moves;
    }
}
