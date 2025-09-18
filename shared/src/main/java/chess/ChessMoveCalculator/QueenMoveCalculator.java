package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessMove;

public class QueenMoveCalculator extends PieceMoveCalculator{

    public QueenMoveCalculator (ChessPosition startingPos, ChessBoard board) {
       super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection [] continuousMovements = {MovementDirection.LEFT, MovementDirection.UPLEFT, MovementDirection.UP, MovementDirection.UPRIGHT, MovementDirection.RIGHT, MovementDirection.DOWNRIGHT, MovementDirection.DOWN, MovementDirection.DOWNLEFT};
        for (int i = 0; i < 8; i++) {
            ChessPosition currentSpace = getNewSpace(startingPos, continuousMovements[i]);
            while (checkAndAddSpace(currentSpace)) {
                currentSpace = getNewSpace(currentSpace, continuousMovements[i]);
            }
        }
    }

    public Collection<ChessMove> getMoves (){
        fillMoves();
        return getMoveList();
    }
}
