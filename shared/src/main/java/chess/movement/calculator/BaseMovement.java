package chess.movement.calculator;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessGame.TeamColor;
import chess.ChessPosition;

public class BaseMovement {

    private ChessBoard board;
    public BaseMovement(ChessBoard board, ChessPiece piece) {
        this.board = board;
    }

    public boolean isInBounds(ChessPosition position) {
        return (position.getRow() < 8 && position.getRow() >= 0 && position.getColumn() < 8 && position.getColumn() >= 0);
    }

    public boolean isSpaceOccupied(ChessPosition position) {
        return (board.getPiece(position) != null);
    }

    public boolean isOpposingPiece(ChessPosition position, TeamColor myColor) {
        return board.getPiece(position).getTeamColor() != myColor;
    }

    public enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN,
        UPLEFT,
        UPRIGHT,
        DOWNRIGHT,
        DOWNLEFT,
        KNIGHTLEFTDOWN,
        KNIGHTLEFTUP,
        KNIGHTUPLEFT,
        KNIGHTUPRIGHT,
        KNIGHTRIGHTUP,
        KNIGHTRIGHTDOWN,
        KNIGHTDOWNRIGHT,
        KNIGHTDOWNLEFT,
        PAWNDOUBLEUP,
        PAWNDOUBLEDOWN
    }


    public ChessPosition getNewSpace(ChessPosition startingPos, Direction direction) {
        if (direction == Direction.UP) {
            return new ChessPosition(startingPos.getRow() + 2, startingPos.getColumn() + 1);
        } else if (direction == Direction.RIGHT) {
            return new ChessPosition(startingPos.getRow() + 1, startingPos.getColumn() + 2);
        } else if (direction == Direction.DOWN) {
            return new ChessPosition(startingPos.getRow() , startingPos.getColumn() + 1);
        } else if (direction == Direction.LEFT) {
            return new ChessPosition(startingPos.getRow() + 1, startingPos.getColumn() );
        } else if (direction == Direction.UPLEFT) {
            return new ChessPosition(startingPos.getRow() + 2, startingPos.getColumn() );
        } else if (direction == Direction.UPRIGHT) {
            return new ChessPosition(startingPos.getRow() + 2, startingPos.getColumn() + 2);
        } else if (direction == Direction.DOWNRIGHT) {
            return new ChessPosition(startingPos.getRow() , startingPos.getColumn() + 2);
        } else if (direction == Direction.DOWNLEFT) {
            return new ChessPosition(startingPos.getRow() , startingPos.getColumn() );
        } else if(direction == Direction.KNIGHTLEFTDOWN) {
            return new ChessPosition(startingPos.getRow() , startingPos.getColumn() - 1);
        } else if(direction == Direction.KNIGHTLEFTUP) {
            return new ChessPosition(startingPos.getRow() + 2, startingPos.getColumn() - 1);
        } else if(direction == Direction.KNIGHTUPLEFT) {
            return new ChessPosition(startingPos.getRow() + 3, startingPos.getColumn() );
        } else if(direction == Direction.KNIGHTUPRIGHT) {
            return new ChessPosition(startingPos.getRow() + 3, startingPos.getColumn() + 2);
        } else if(direction == Direction.KNIGHTRIGHTUP) {
            return new ChessPosition(startingPos.getRow() + 2, startingPos.getColumn() + 3);
        } else if (direction == Direction.KNIGHTRIGHTDOWN) {
            return new ChessPosition(startingPos.getRow() , startingPos.getColumn() + 3);
        } else if (direction == Direction.KNIGHTDOWNRIGHT) {
            return new ChessPosition(startingPos.getRow() - 1, startingPos.getColumn() + 2);
        } else if (direction == Direction.KNIGHTDOWNLEFT) {
            return new ChessPosition(startingPos.getRow() - 1, startingPos.getColumn() );
        } else if (direction == Direction.PAWNDOUBLEUP) {
            return new ChessPosition(startingPos.getRow() + 3, startingPos.getColumn() + 1);
        } else if (direction == Direction.PAWNDOUBLEDOWN) {
            return new ChessPosition(startingPos.getRow() - 1, startingPos.getColumn() + 1);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
