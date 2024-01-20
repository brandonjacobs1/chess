package chess;

public class Main {
    public static void main(String[] args) {
        ChessPiece knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        System.out.print(knight.pieceMoves(new ChessBoard(), new ChessPosition(1, 6)));
    }

}
