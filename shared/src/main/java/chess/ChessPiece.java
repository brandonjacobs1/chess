package chess;

import chess.ChessGame.TeamColor;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    PieceType pieceType;
    TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
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
        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.pieceType) {
            case KING -> MoveCalculator.calculateKingMoves(myPosition, board, this.teamColor);
            case QUEEN -> MoveCalculator.calculateQueenMoves(myPosition, board, this.teamColor);
            case BISHOP -> MoveCalculator.calculateBishopMoves(myPosition, board, this.teamColor);
            case KNIGHT -> MoveCalculator.calculateKnightMoves(myPosition, board, this.teamColor);
            case ROOK -> MoveCalculator.calculateRookMoves(myPosition, board, this.teamColor);
            case PAWN -> MoveCalculator.calculatePawnMoves(myPosition, board, this.teamColor);
        };
    }


    @Override
    public String toString() {
        String pieceLetter = switch (this.pieceType) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };

        // Adjust letter case based on team color
        if (this.teamColor == TeamColor.BLACK) {
            return pieceLetter.toLowerCase();
        } else {
            return pieceLetter.toUpperCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }
}
