package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove chessMove;

    public MakeMoveCommand(ChessMove chessMove, int gameID, String authentication) {
        super(CommandType.MAKE_MOVE, authentication, gameID);
        this.chessMove = chessMove;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }
}
