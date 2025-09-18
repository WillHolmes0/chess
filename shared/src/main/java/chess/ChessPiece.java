package chess;

import chess.ChessMoveCalculator.*;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.PAWN) {
            return new PawnMoveCalculator(myPosition, board).getMoves();
        } else if (type == PieceType.KING) {
            return new KingMoveCalculator(myPosition, board).getMoves();
        } else if (type == PieceType.QUEEN) {
            return new QueenMoveCalculator(myPosition, board).getMoves();
        } else if (type == PieceType.ROOK) {
            return new RookMoveCalculator(myPosition, board).getMoves();
        } else if (type == PieceType.BISHOP) {
            return new BishopMoveCalculator(myPosition, board).getMoves();
        } else if (type == PieceType.KNIGHT) {
            return new KnightMoveCalculator(myPosition, board).getMoves();
        } else {
            throw new RuntimeException("The Correct piece type is not found");
        }
    }
}
