package chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece [][] board;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getColumn() - 1][position.getRow() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getColumn() - 1][position.getRow() - 1];
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color){
        for (int row=8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = getPiece(position);
                if (piece != null){
                    if (ChessPiece.PieceType.KING == piece.getPieceType() && piece.getTeamColor() == color){
                        return position;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] pieces = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };

        for (int col=1; col<9; col++){
            this.addPiece(new ChessPosition(1, col), new ChessPiece(ChessGame.TeamColor.WHITE, pieces[col-1]));
            this.addPiece(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(8, col), new ChessPiece(ChessGame.TeamColor.BLACK, pieces[col-1]));

        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int row=8; row > 0; row--){
            for (int col=1; col < 9; col++){
                ChessPiece piece = getPiece(new ChessPosition(row, col));
                if (piece != null){
                    output.append(piece).append(" ");
                } else {
                    output.append("  ");
                }
            }
            output.append('\n');
        }
        return output.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
