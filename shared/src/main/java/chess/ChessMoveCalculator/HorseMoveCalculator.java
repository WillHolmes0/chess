package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;

public class HorseMoveCalculator extends PieceMoveCalculator{

    public HorseMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        MovementDirection []singleMovements = {
                MovementDirection.KNIGHTLEFTDOWN,
                MovementDirection.KNIGHTLEFTUP,
                MovementDirection.KNIGHTUPLEFT,
                MovementDirection.KNIGHTUPRIGHT,
                MovementDirection.KNIGHTRIGHTUP,
                MovementDirection.KNIGHTRIGHTDOWN,
                MovementDirection.KNIGHTDOWNRIGHT,
                MovementDirection.KNIGHTDOWNLEFT};
        for (int i = 0; i < 8; i++) {
            checkAndAddSpace(getNewSpace(startingPos, singleMovements[i]));
        }
    }

    public Collection<ChessPosition> getMoves () {
        fillMoves();
        return getMoveList();
    }
}