package chess.MoveCalculators;

import chess.*;

import java.util.ArrayList;

public class PawnMove {
    public static ArrayList<ChessMove> calculatePawnMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {

        int moveMultiplier = (ChessGame.TeamColor.BLACK == startColor) ? -1 : 1;
        ArrayList<ChessPosition>  positions = new ArrayList<>();

        positions.add(new ChessPosition(moveMultiplier, 0));
        positions.add(new ChessPosition(2 * moveMultiplier, 0));
        positions.add(new ChessPosition(moveMultiplier, -1 * moveMultiplier));
        positions.add(new ChessPosition(moveMultiplier, moveMultiplier));

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
            boolean isOpposingTeam = isOpposingTeam(startColor, newSpace.getTeamColor());
            if (isDiagonal && isOpposingTeam) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPromotion (ChessPosition end) {
        return end.getRow() == 1 || end.getRow() == 8;
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
                    boolean isPromotion = isPromotion(endPosition);
                    if (isPromotion) {
                        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(startPosition, endPosition, null));
                    }
                }
            }
        }
        return moves;
    }
}
