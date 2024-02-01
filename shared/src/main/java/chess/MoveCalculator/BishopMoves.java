package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoves {
    public static ArrayList<ChessMove> getValidMoves (ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color) {
        return SharedMoves.checkDiagonalLine(startPosition, board, color);

    }
}
