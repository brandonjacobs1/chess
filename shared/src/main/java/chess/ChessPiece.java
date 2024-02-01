package chess;

import chess.MoveCalculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.type) {
            case KING -> KingMoves.getValidMoves(board, myPosition, this.color);
            case QUEEN -> QueenMoves.getValidMoves(board, myPosition, this.color);
            case BISHOP -> BishopMoves.getValidMoves(board, myPosition, this.color);
            case KNIGHT -> KnightMoves.getValidMoves(board, myPosition, this.color);
            case ROOK -> RookMoves.getValidMoves(board, myPosition, this.color);
            case PAWN -> PawnMoves.getValidMoves(board, myPosition, this.color);
        };
    }

    @Override
    public String toString() {
        String piece = switch (this.type){
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
        if (this.color == ChessGame.TeamColor.BLACK) {
            piece = piece.toLowerCase();
        } else {
            piece = piece.toUpperCase();
        }

        return piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
