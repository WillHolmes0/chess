package chess.ChessMoveCalculator;

import java.util.Collection;
import java.util.ArrayList;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessGame;

public class PieceMoveCalculator {

    ChessPosition startingPos;
    ChessBoard board;
    Collection<ChessPosition> moves;

    public PieceMoveCalculator (ChessPosition startingPos, ChessBoard board) {
        this.startingPos = startingPos;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    protected boolean isInBounds(ChessPosition pos) {
        return (pos.getRow() <= 8 && pos.getRow() > 0 && pos.getColumn() <= 8 && pos.getColumn() > 0);
    }

    protected boolean isSpaceEmpty(ChessPosition pos) {
        return (board.getPiece(pos) == null);
    }

    protected boolean isOpposingPiece(ChessPosition pos) {
        return (board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK);
    }

    protected boolean isOpenSpace(ChessPosition pos) {

        return (isInBounds(pos) && isSpaceEmpty(pos));
    }

    protected boolean checkAndAddSpace(ChessPosition pos) {
        if (isOpenSpace(pos)) {
            moves.add(pos);
            return true;
        } else if (isOpposingPiece((pos))) {
            moves.add(pos);
        }
        return false;
    }

    public enum MovementDirection {
        LEFT,
        UPLEFT,
        UP,
        UPRIGHT,
        RIGHT,
        DOWNLEFT,
        DOWN,
        DOWNRIGHT,
        KNIGHTLEFTDOWN,
        KNIGHTLEFTUP,
        KNIGHTUPLEFT,
        KNIGHTUPRIGHT,
        KNIGHTRIGHTUP,
        KNIGHTRIGHTDOWN,
        KNIGHTDOWNRIGHT,
        KNIGHTDOWNLEFT
    }

    protected ChessPosition getNewSpace(ChessPosition pos, MovementDirection direction) {
        if (direction == MovementDirection.LEFT) {
            return new ChessPosition(pos.getRow(), pos.getColumn() - 1);
        } else if (direction == MovementDirection.UPLEFT) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() - 1);
        } else if (direction == MovementDirection.UP) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn());
        } else if (direction == MovementDirection.UPRIGHT) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() + 1);
        } else if (direction == MovementDirection.RIGHT) {
            return new ChessPosition(pos.getRow(), pos.getColumn() + 1);
        } else if (direction == MovementDirection.DOWNRIGHT) {
            return new ChessPosition(pos.getRow() - 1, pos.getColumn() + 1);
        } else if (direction == MovementDirection.DOWN) {
            return new ChessPosition(pos.getRow() - 1, pos.getColumn());
        } else if (direction == MovementDirection.DOWNLEFT) {
            return new ChessPosition(pos.getRow() - 1, pos.getColumn() - 1);
        } else if (direction == MovementDirection.KNIGHTLEFTUP) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() - 2);
        } else if (direction == MovementDirection.KNIGHTLEFTDOWN) {
            return new ChessPosition(pos.getRow() - 1, pos.getColumn() - 2);
        } else if (direction == MovementDirection.KNIGHTUPLEFT) {
            return new ChessPosition(pos.getRow() + 2, pos.getColumn() - 1);
        } else if (direction == MovementDirection.KNIGHTUPRIGHT) {
            return new ChessPosition(pos.getRow() + 2, pos.getColumn() + 1);
        } else if (direction == MovementDirection.KNIGHTRIGHTUP) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() + 2);
        } else if (direction == MovementDirection.KNIGHTRIGHTDOWN) {
            return new ChessPosition(pos.getRow() - 1, pos.getColumn() + 2);
        } else if (direction == MovementDirection.KNIGHTDOWNRIGHT) {
            return new ChessPosition(pos.getRow() - 2, pos.getColumn() + 1);
        } else if (direction == MovementDirection.KNIGHTDOWNLEFT) {
            return new ChessPosition(pos.getRow() - 2, pos.getColumn() - 1);
        } else {
            throw new IllegalArgumentException("Did not provide a valid direction");
        }
    }

    protected Collection<ChessPosition> getMoveList() {
        return moves;
    }
}
