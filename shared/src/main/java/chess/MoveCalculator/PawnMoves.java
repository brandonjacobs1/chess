package chess.MoveCalculator;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {

    private static boolean isPromotion (ChessMove move, ChessGame.TeamColor color) {
        int endRow = move.getEndPosition().getRow();
        if (color == ChessGame.TeamColor.BLACK) {
            return endRow == 1;
        } else {
            return endRow == 8;
        }
    }

    private static boolean isDiagonal (ChessPosition start, ChessPosition end) {
        int startRow = start.getRow();
        int startCol = start.getColumn();
        int endRow = end.getRow();
        int endCol = end.getColumn();

        return Math.abs(startRow - endRow) == 1 && Math.abs(startCol - endCol) == 1;
    }


    private static boolean isFirstMove (ChessPosition pos, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.BLACK){
            return pos.getRow() == 7;
        } else if (color == ChessGame.TeamColor.WHITE) {
            return pos.getRow() == 2;
        }
        return false;
    }

    private static boolean is2SpaceMove (ChessPosition start, ChessPosition end) {
        return Math.abs(start.getRow() - end.getRow()) == 2;
    }

    private static boolean isValid(ChessPosition startPosition, ChessPosition endPosition, ChessGame.TeamColor startColor, ChessBoard board) {
        boolean isInBounds = SharedMoves.isInBounds(endPosition);

        if (isInBounds) {
            ChessPiece endPiece = board.getPiece(endPosition);
            boolean isDiagonal = isDiagonal(startPosition, endPosition);
            boolean isFirstMove = isFirstMove(startPosition, startColor);
            boolean is2SpaceMove = is2SpaceMove(startPosition, endPosition);

            if (isDiagonal) {
                if (endPiece != null) {
                    boolean isSameTeam = SharedMoves.isSameTeam(startColor, endPiece.getTeamColor());
                    return !isSameTeam;
                }
            } else {
                if (is2SpaceMove) {
                    if (isFirstMove) {
                        ChessPiece skip;
                        if (startColor == ChessGame.TeamColor.WHITE) {
                            skip = board.getPiece(new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn()));
                        } else {
                            skip = board.getPiece(new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn()));
                        }
                        if (skip == null) {
                            return endPiece == null;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return endPiece == null;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public static ArrayList<ChessMove> getValidMoves (ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int moveMultiplier = ChessGame.TeamColor.BLACK == color ? -1 : 1;
        ChessMove[] testMoves = {
                new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + (moveMultiplier), startPosition.getColumn()), null),
                new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + (2 * moveMultiplier), startPosition.getColumn()), null),
                new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + (moveMultiplier), startPosition.getColumn() - 1), null),
                new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + (moveMultiplier), startPosition.getColumn() + 1), null)
        };

        for (ChessMove move : testMoves) {
            boolean isValid = isValid(move.getStartPosition(), move.getEndPosition(), color, board);
            boolean isPromotion = isPromotion(move, color);
            if (isValid) {
                if (isPromotion) {
                    moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                } else {
                    moves.add(move);
                }
            }
        }

        return moves;
    }
}
