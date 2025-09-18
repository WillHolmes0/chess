package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;

public class KingMoveCalculator extends PieceMoveCalculator{

    public KingMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection []singleMovements = {MovementDirection.LEFT, MovementDirection.UPLEFT, MovementDirection.UP, MovementDirection.UPRIGHT, MovementDirection.RIGHT, MovementDirection.DOWNRIGHT, MovementDirection.DOWN, MovementDirection.DOWNLEFT};
        for (int i = 0; i < 8; i++) {
            checkAndAddSpace(getNewSpace(startingPos, singleMovements[i]));
        }
    }

    public Collection<ChessPosition> getMoves () {
        fillMoves();
        return getMoveList();
    }
}