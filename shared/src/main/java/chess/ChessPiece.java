package chess;

import chess.ChessGame.TeamColor;
import chess.PieceMoves.KnightMoves;
import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;
import java.util.ArrayList;

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
        ArrayList<ChessMove> moves = null;
        switch (this.pieceType) {
            case KING:
                KnightMoves knight =  new KnightMoves();
                moves = knight.getKnightMoves(board, myPosition);
                break;
            case QUEEN:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }
        return moves;
    }


    @Override
    public String toString() {
        String pieceLetter = "";

        switch (this.pieceType) {
            case KING:
                pieceLetter = "K";
                break;
            case QUEEN:
                pieceLetter = "Q";
                break;
            case BISHOP:
                pieceLetter = "B";
                break;
            case KNIGHT:
                pieceLetter = "N";
                break;
            case ROOK:
                pieceLetter = "R";
                break;
            case PAWN:
                pieceLetter = "P";
                break;
        }

        // Adjust letter case based on team color
        if (this.teamColor == TeamColor.BLACK) {
            return pieceLetter.toLowerCase();
        } else {
            return pieceLetter.toUpperCase();
        }
    }

}
