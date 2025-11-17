package chess.movement.calculator;

import java.util.Collection;
import java.util.ArrayList;

import chess.*;

public class PawnMovement extends BaseMovement {

    private ChessBoard board;
    private ChessPosition startingPosition;
    private ChessPiece piece;
    private Collection<ChessMove> moves;
    private Direction[] attackOptions;

    public PawnMovement(ChessBoard board, ChessPiece piece, ChessPosition startingPosition) {
        super(board, piece);
        this.board = board;
        this.piece = piece;
        this.startingPosition = startingPosition;
        this.moves = new ArrayList<>();
        this.attackOptions = new Direction[2];
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            attackOptions[0] = Direction.UPLEFT;
            attackOptions[1] = Direction.UPRIGHT;
        } else {
            attackOptions[0] = Direction.DOWNLEFT;
            attackOptions[1] = Direction.DOWNRIGHT;
        }
    }

    private void pawnMoveToSpace(ChessPosition potentialPosition) {
        if (potentialPosition.getRow() == 7 || potentialPosition.getRow() == 0) {
            moves.add(new ChessMove(startingPosition, potentialPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(startingPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(startingPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(startingPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
        } else {
            moves.add(new ChessMove(startingPosition, potentialPosition, null));
        }
    }

    public void findMoves() {
        for (int i = 0; i < attackOptions.length; i++) {
            ChessPosition potentialPosition = getNewSpace(startingPosition, attackOptions[i]);
            if (isInBounds(potentialPosition)) {
                if (isSpaceOccupied(potentialPosition) && isOpposingPiece(potentialPosition, piece.getTeamColor())) {
                    pawnMoveToSpace(potentialPosition);
                }
            }
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition potentialPosition = getNewSpace(startingPosition, Direction.UP);
            if (isInBounds(potentialPosition)) {
                if (!isSpaceOccupied(potentialPosition)) {
                    pawnMoveToSpace(potentialPosition);
                    if (startingPosition.getRow() == 1) {
                        ChessPosition doubleMovePosition = getNewSpace(startingPosition, Direction.PAWNDOUBLEUP);
                        if (!isSpaceOccupied(doubleMovePosition)) {
                            pawnMoveToSpace(doubleMovePosition);
                        }
                    }
                }
            }
        } else {
            ChessPosition potentialPosition = getNewSpace(startingPosition, Direction.DOWN);
            if (isInBounds(potentialPosition)) {
                if (!isSpaceOccupied(potentialPosition)) {
                    pawnMoveToSpace(potentialPosition);
                    if (startingPosition.getRow() == 6) {
                        ChessPosition doubleMovePosition = getNewSpace(startingPosition, Direction.PAWNDOUBLEDOWN);
                        if (!isSpaceOccupied(doubleMovePosition)) {
                            pawnMoveToSpace(doubleMovePosition);
                        }
                    }
                }
            }
        }

    }

    public Collection<ChessMove> getMoves() {
        return moves;
    }
}
