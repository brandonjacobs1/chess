package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMoves {
    public static ArrayList<ChessMove> getValidMoves (ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(SharedMoves.checkStraightLine(startPosition, board, color));
        moves.addAll(SharedMoves.checkDiagonalLine(startPosition, board, color));
        return moves;
    }
}
