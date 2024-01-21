package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

import static chess.MoveCalculators.SharedMoves.checkAbsolutePositions;

public class KnightMove {
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
}
