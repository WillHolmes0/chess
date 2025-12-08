package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove move;

    public MakeMoveCommand(ChessMove chessMove, int gameID, String authentication) {
        super(CommandType.MAKE_MOVE, authentication, gameID);
        this.move = chessMove;
    }

    public ChessMove getChessMove() {
        return move;
    }
}
