package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.ChessPosition;
import chess.ChessBoard;

public class PawnMoveCalculator extends PieceMoveCalculator{

    public PawnMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        super(startingPos, board);
    }

    public void fillMoves() {
        checkAndAddSpace(getNewSpace(startingPos, MovementDirection.UPLEFT));
        checkAndAddSpace(getNewSpace(startingPos, MovementDirection.UPRIGHT));
        checkAndAddSpace(getNewSpace(startingPos, MovementDirection.UP));
        if (startingPos.getRow() == 2) {
            checkAndAddSpace(getNewSpace(startingPos, MovementDirection.PAWNDOUBLEADVANCE));
        }
    }

    public Collection<ChessPosition> getMoves () {
        fillMoves();
        return getMoveList();
    }
}