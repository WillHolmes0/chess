package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessMove;

public class KnightMoveCalculator extends PieceMoveCalculator{

    public KnightMoveCalculator(ChessPosition startingPos, ChessBoard board) {
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

    public Collection<ChessMove> getMoves () {
        fillMoves();
        return getMoveList();
    }
}