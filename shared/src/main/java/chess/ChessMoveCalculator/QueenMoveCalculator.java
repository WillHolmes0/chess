package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;

public class QueenMoveCalculator extends PieceMoveCalculator{

    public QueenMoveCalculator (ChessPosition startingPos, ChessBoard board) {
       super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection [] continuousMovements = {MovementDirection.LEFT, MovementDirection.UPLEFT, MovementDirection.UP, MovementDirection.UPRIGHT, MovementDirection.RIGHT, MovementDirection.DOWNRIGHT, MovementDirection.DOWN, MovementDirection.DOWNLEFT};
        for (int i = 0; i < 8; i++) {
            ChessPosition currentSpace = getNewSpace(startingPos, MovementDirection.UPRIGHT);
            while (checkAndAddSpace(currentSpace)) {
                currentSpace = getNewSpace(currentSpace, MovementDirection.UPLEFT);
            }
        }
    }

    public Collection<ChessPosition> getMoves (){
        fillMoves();
        return getMoveList();
    }
}
