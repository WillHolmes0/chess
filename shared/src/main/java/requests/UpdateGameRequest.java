package requests;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;

public record UpdateGameRequest(ChessMove chessMove, int gameID, String authorization) {
}
