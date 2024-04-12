package model;

import chess.ChessGame.TeamColor;

public record JoinGameBody(TeamColor color, int gameID) {

}
