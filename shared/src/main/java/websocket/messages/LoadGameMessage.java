package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    ChessGame chessGame;

    public LoadGameMessage(ChessGame chessGame) {
        super(ServerMessageType.LOAD_GAME);
        this.chessGame = chessGame;
    }

    public ChessGame getGame() {
        return chessGame;
    }
}
