package chess.ChessMoveCalculator;

import java.util.Collection;
import chess.*;

public class PawnMoveCalculator extends PieceMoveCalculator{

    private ChessPiece piece;

    public PawnMoveCalculator (ChessPosition startingPos, ChessBoard board, ChessPiece piece) {
        super(startingPos, board);
        this.piece = piece;
    }

    public void fillMoves() {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition upLeft = getNewSpace(startingPos, MovementDirection.UPLEFT);
            if (isInBounds(upLeft) && board.getPiece(upLeft) != null) {
                if (board.getPiece(upLeft).getTeamColor() != ChessGame.TeamColor.WHITE) {
                    if (upLeft.getRow() == 7) {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPLEFT), ChessPiece.PieceType.QUEEN));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPLEFT), ChessPiece.PieceType.BISHOP));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPLEFT), ChessPiece.PieceType.KNIGHT));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPLEFT), ChessPiece.PieceType.ROOK));
                    } else {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPLEFT), null));
                    }
                }
            }
            ChessPosition upRight = getNewSpace(startingPos, MovementDirection.UPRIGHT);
            if (isInBounds(upRight) && board.getPiece(upRight) != null) {
                if (board.getPiece(upRight).getTeamColor() != ChessGame.TeamColor.WHITE) {
                    if (upRight.getRow() == 7) {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPRIGHT), ChessPiece.PieceType.QUEEN));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPRIGHT), ChessPiece.PieceType.BISHOP));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPRIGHT), ChessPiece.PieceType.KNIGHT));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPRIGHT), ChessPiece.PieceType.ROOK));
                    } else {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UPRIGHT), null));
                    }
                }
            }
            if (isInBounds(getNewSpace(startingPos, MovementDirection.UP)) && isOpenSpace(getNewSpace(startingPos, MovementDirection.UP))) {
                if (getNewSpace(startingPos, MovementDirection.UP).getRow() == 7) {
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UP), ChessPiece.PieceType.QUEEN));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UP), ChessPiece.PieceType.BISHOP));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UP), ChessPiece.PieceType.KNIGHT));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UP), ChessPiece.PieceType.ROOK));
                } else {
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.UP), null));
                }
                if (startingPos.getRow() == 1) {
                    if (isOpenSpace(getNewSpace(startingPos, MovementDirection.PAWNDOUBLEUP))) {
                        moves.add(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.PAWNDOUBLEUP), null));
                    }
                }
            }
        } else {
            ChessPosition downLeft = getNewSpace(startingPos, MovementDirection.DOWNLEFT);
            if (isInBounds(downLeft) && board.getPiece(downLeft) != null) {
                if (board.getPiece(downLeft).getTeamColor() != ChessGame.TeamColor.BLACK) {
                    if (downLeft.getRow() == 0) {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNLEFT), ChessPiece.PieceType.QUEEN));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNLEFT), ChessPiece.PieceType.BISHOP));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNLEFT), ChessPiece.PieceType.KNIGHT));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNLEFT), ChessPiece.PieceType.ROOK));
                    } else {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNLEFT), null));
                    }
                }
            }
            ChessPosition downRight = getNewSpace(startingPos, MovementDirection.DOWNRIGHT);
            if (isInBounds(downRight) && board.getPiece(downRight) != null) {
                if (board.getPiece(downRight).getTeamColor() != ChessGame.TeamColor.BLACK) {
                    if (downRight.getRow() == 0) {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNRIGHT), ChessPiece.PieceType.QUEEN));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNRIGHT), ChessPiece.PieceType.BISHOP));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNRIGHT), ChessPiece.PieceType.KNIGHT));
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNRIGHT), ChessPiece.PieceType.ROOK));
                    } else {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWNRIGHT), null));
                    }                }
            }
            if (isInBounds(getNewSpace(startingPos, MovementDirection.DOWN)) && isOpenSpace(getNewSpace(startingPos, MovementDirection.DOWN))) {
                if (downRight.getRow() == 0) {
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWN), ChessPiece.PieceType.QUEEN));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWN), ChessPiece.PieceType.BISHOP));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWN), ChessPiece.PieceType.KNIGHT));
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWN), ChessPiece.PieceType.ROOK));
                } else {
                    addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.DOWN), null));
                }
                if (startingPos.getRow() == 6) {
                    if (isOpenSpace(getNewSpace(startingPos, MovementDirection.PAWNDOUBLEDOWN))) {
                        addMove(new ChessMove(startingPos, getNewSpace(startingPos, MovementDirection.PAWNDOUBLEDOWN), null));
                    }
                }
            }
        }
    }

    public Collection<ChessMove> getMoves () {
        fillMoves();
        return getMoveList();
    }
}