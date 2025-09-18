package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;

public class RookMoveCalculator extends PieceMoveCalculator {

    public RookMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection [] continuousMovements = {MovementDirection.LEFT, MovementDirection.UP, MovementDirection.RIGHT, MovementDirection.DOWN};
        for (int i = 0; i < 8; i++) {
            ChessPosition currentSpace = getNewSpace(startingPos, continuousMovements[i]);
            while (checkAndAddSpace(currentSpace)) {
                currentSpace = getNewSpace(currentSpace, continuousMovements[i]);
            }
        }
    }

    public Collection<ChessPosition> getMoves (){
        fillMoves();
        return getMoveList();
    }
}