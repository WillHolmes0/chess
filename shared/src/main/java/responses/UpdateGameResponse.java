package responses;

import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

public record UpdateGameResponse(ChessGame.TeamColor color, String username, String chessPiece, String startPosition, String endPosition, GameData gameData) {
}
