package chess.MovementCalculator;

import java.util.Collection;
import java.util.ArrayList;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

public class RookMovement extends BaseMovement{

    private ChessBoard board;
    private ChessPosition startingPosition;
    private ChessPiece piece;
    private Collection<ChessMove> moves;
    private BaseMovement.Direction[] options = {
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT};

    public RookMovement(ChessBoard board, ChessPiece piece, ChessPosition startingPosition) {
        super(board, piece);
        this.board = board;
        this.piece = piece;
        this.startingPosition = startingPosition;
        this.moves = new ArrayList<>();
    }

    public void findMoves() {
        for (int i = 0; i < options.length; i++) {
            boolean clear = true;
            ChessPosition currentPosition = startingPosition;
            while (clear) {
                ChessPosition potentialPosition = getNewSpace(currentPosition, options[i]);
                if (isInBounds(potentialPosition)) {
                    if (!isSpaceOccupied(potentialPosition)) {
                        ChessMove newMove = new ChessMove(startingPosition, potentialPosition, null);
                        moves.add(newMove);
                        currentPosition = potentialPosition;
                    } else {
                        clear = false;
                        if (isOpposingPiece(potentialPosition, piece.getTeamColor())) {
                            ChessMove newMove = new ChessMove(startingPosition, potentialPosition, null);
                            moves.add(newMove);
                        }
                    }
                } else {
                    clear = false;
                }
            }
        }
    }

    public Collection<ChessMove> getMoves() {
        return moves;
    }
}