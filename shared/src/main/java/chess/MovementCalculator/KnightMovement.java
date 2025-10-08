package chess.MovementCalculator;

import java.util.Collection;
import java.util.ArrayList;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

public class KnightMovement extends BaseMovement{

    private ChessBoard board;
    private ChessPosition startingPosition;
    private ChessPiece piece;
    private Collection<ChessMove> moves;
    private BaseMovement.Direction[] options = {
            Direction.KNIGHTDOWNLEFT,
            Direction.KNIGHTDOWNRIGHT,
            Direction.KNIGHTUPLEFT,
            Direction.KNIGHTUPRIGHT,
            Direction.KNIGHTLEFTUP,
            Direction.KNIGHTLEFTDOWN,
            Direction.KNIGHTRIGHTDOWN,
            Direction.KNIGHTRIGHTUP};

    public KnightMovement(ChessBoard board, ChessPiece piece, ChessPosition startingPosition) {
        super(board, piece);
        this.board = board;
        this.piece = piece;
        this.startingPosition = startingPosition;
        this.moves = new ArrayList<>();
    }

    public void findMoves() {
        for (int i = 0; i < options.length; i++) {
            ChessPosition potentialPosition = getNewSpace(startingPosition, options[i]);
            if (isInBounds(potentialPosition)) {
                if (!isSpaceOccupied(potentialPosition)) {
                    ChessMove newMove = new ChessMove(startingPosition, potentialPosition, null);
                    moves.add(newMove);
                } else {
                    if (isOpposingPiece(potentialPosition, piece.getTeamColor())) {
                        ChessMove newMove = new ChessMove(startingPosition, potentialPosition, null);
                        moves.add(newMove);
                    }
                }
            }
        }
    }

    public Collection<ChessMove> getMoves() {
        return moves;
    }
}