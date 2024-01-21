package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMove {
    public static ArrayList<ChessMove> calculateQueenMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor startColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        //Check vertical/horizontal
        SharedMoves.straightLineMove(moves, startPosition, board, startColor, startRow, startCol);
        // Check diagonal
        SharedMoves.diagonalLineMove(moves, startPosition, board, startColor, startRow, startCol);

        return moves;
    }
}
