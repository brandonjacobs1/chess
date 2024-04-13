package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamColor;

    ChessBoard board;

    boolean isComplete = false;
    public ChessGame() {
        teamColor = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        var moves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        if (piece != null) {
            for (ChessMove move : moves){
                ChessPosition endPosition = move.getEndPosition();
                ChessPiece endPiece = board.getPiece(endPosition);

                // Test moves
                board.addPiece(endPosition, piece);
                board.addPiece(startPosition, null);

                boolean isInCheck = isInCheck(piece.getTeamColor());
                if (!isInCheck) {
                    validMoves.add(move);
                }

                // Reset move
                board.addPiece(startPosition, piece);
                board.addPiece(endPosition, endPiece);
            }
            return validMoves;
        } else {
            return null;
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPosition endPosition = move.getEndPosition();
        ChessPosition startPosition = move.getStartPosition();
        var validMoves = validMoves(startPosition);
        if (piece != null) {
            if (teamColor != piece.getTeamColor()) throw new InvalidMoveException("Invalid Move");

            if (validMoves.contains(move)) {
                // Add move
                if (move.getPromotionPiece() != null) {
                    piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                }
                board.addPiece(endPosition, piece);
                board.addPiece(startPosition, null);

            } else {
                throw new InvalidMoveException("Invalid Move");
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
        if (teamColor == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        for (int row=8; row > 0; row--){
            for (int col=1; col < 9; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        var moves = piece.pieceMoves(board, new ChessPosition(row, col));
                        for(ChessMove move : moves){
                            ChessPosition end = move.getEndPosition();
                            if(end.equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        var kingPosition = board.getKingPosition(teamColor);
        var validMoves = validMoves(kingPosition);
        boolean isInCheck = isInCheck(teamColor);
        if(validMoves.isEmpty() && isInCheck) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == this.teamColor) {
            var piecePositions = board.getAllPieces(teamColor);
            for (var position : piecePositions) {
                var validMoves = validMoves(position);
                if (!validMoves.isEmpty()) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
