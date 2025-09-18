package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessMove;

public class BishopMoveCalculator extends PieceMoveCalculator{

    public BishopMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection [] continuousMovements = {MovementDirection.UPLEFT, MovementDirection.UPRIGHT, MovementDirection.DOWNRIGHT, MovementDirection.DOWNLEFT};
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
