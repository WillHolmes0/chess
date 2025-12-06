package responses;

import chess.ChessGame;
import model.GameData;

public record RetrievePlayerGameResponse(ChessGame chessGame, String role, String username) {
}
