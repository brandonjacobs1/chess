package model;

import chess.ChessGame.TeamColor;

public record JoinGameBody(TeamColor playerColor, int gameID) {

}
