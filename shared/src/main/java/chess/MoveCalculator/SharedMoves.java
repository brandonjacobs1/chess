package chess.MoveCalculator;

import chess.*;

import java.util.ArrayList;

public class SharedMoves {

    public static boolean isInBounds(ChessPosition end){
        return end.getRow() < 9 && end.getRow() > 0 && end.getColumn() < 9 && end.getColumn() > 0;
    }

    public static boolean isSameTeam (ChessGame.TeamColor startColor, ChessGame.TeamColor endColor) {
        return startColor == endColor;
    }
    public static boolean isAbsPosValid (ChessMove move, ChessBoard board, ChessGame.TeamColor startColor) {
        boolean isInBounds = isInBounds(move.getEndPosition());
        if (isInBounds){
            ChessPiece endPiece = board.getPiece(move.getEndPosition());
            if (endPiece == null) {
                return true;
            } else {
                return !isSameTeam(startColor, endPiece.getTeamColor());
            }
        }
        return false;
    }

    private static ArrayList<ChessMove> checkLine (ChessPosition start, int changeRow, int changeCol, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int endRow = start.getRow() + changeRow;
        int endCol = start.getColumn() + changeCol;
        ChessPosition endPosition = new ChessPosition(endRow, endCol);
        while (isInBounds(endPosition)) {
            ChessPiece endPiece = board.getPiece(endPosition);
            if (endPiece == null) {
                moves.add(new ChessMove(start, endPosition, null));
            } else {
                boolean isSameTeam = isSameTeam(startColor, endPiece.getTeamColor());
                if (!isSameTeam) {
                    moves.add(new ChessMove(start, endPosition, null));
                }
                break;
            }

            endPosition = new ChessPosition(endPosition.getRow() + changeRow, endPosition.getColumn() + changeCol);
        }

        return moves;
    }

    public static ArrayList<ChessMove> checkStraightLine (ChessPosition start, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(checkLine(start, 1, 0, board, startColor));
        moves.addAll(checkLine(start, 0, 1, board, startColor));
        moves.addAll(checkLine(start, -1, 0, board, startColor));
        moves.addAll(checkLine(start, 0, -1, board, startColor));


        return moves;
    }

    public static ArrayList<ChessMove> checkDiagonalLine (ChessPosition start, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(checkLine(start, 1, 1, board, startColor));
        moves.addAll(checkLine(start, -1, 1, board, startColor));
        moves.addAll(checkLine(start, -1, -1, board, startColor));
        moves.addAll(checkLine(start, 1, -1, board, startColor));


        return moves;
    }

}
