package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMove {
    public static ArrayList<ChessMove> calculateRookMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        SharedMoves.straightLineMove(moves, startPosition, board, startColor, startRow, startCol);

        return moves;
    }
}
