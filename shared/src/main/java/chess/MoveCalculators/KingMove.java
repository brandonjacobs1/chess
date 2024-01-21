package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

import static chess.MoveCalculators.SharedMoves.checkAbsolutePositions;

public class KingMove {
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
}
