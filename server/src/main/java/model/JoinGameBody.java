package model;

public record JoinGameBody(Color playerColor, int gameID) {
    public enum Color {
         WHITE,
        BLACK
    }
}
