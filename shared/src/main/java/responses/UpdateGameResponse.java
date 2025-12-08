package responses;

import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

public record UpdateGameResponse(String username, String chessPiece, String startPosition, String endPosition, GameData gameData) {
}
