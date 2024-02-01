package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves {
    public static ArrayList<ChessMove> getValidMoves (ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        ArrayList<ChessMove> testMoves = new ArrayList<>();
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() -2, startPosition.getColumn() - 1), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() -1, startPosition.getColumn() - 2), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2), null));
        testMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1), null));

        for (ChessMove move : testMoves) {
            boolean isValid = SharedMoves.isAbsPosValid(move, board, color);
            if (isValid) moves.add(move);
        }

        return moves;
    }
}
