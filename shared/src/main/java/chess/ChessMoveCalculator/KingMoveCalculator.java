package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessMove;

public class KingMoveCalculator extends PieceMoveCalculator{

    public KingMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection []singleMovements = {MovementDirection.LEFT, MovementDirection.UPLEFT, MovementDirection.UP, MovementDirection.UPRIGHT, MovementDirection.RIGHT, MovementDirection.DOWNRIGHT, MovementDirection.DOWN, MovementDirection.DOWNLEFT};
        for (int i = 0; i < 8; i++) {
            System.out.println(i);
            checkAndAddSpace(getNewSpace(startingPos, singleMovements[i]));
        }
    }

    public Collection<ChessMove> getMoves () {
        fillMoves();
        return getMoveList();
    }
}