package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard chessBoard;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        chessBoard = new ChessBoard();
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        teamTurn = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : allMoves) {
            if (!putsInCheck(move)) {
                validMoves.add(move);
                printMove(move);
            }
        }
        return validMoves;
    }

    public boolean putsInCheck(ChessMove potentialMove) {
        ChessPiece piece = chessBoard.getPiece(potentialMove.getStartPosition());
        ChessPiece takenPiece = chessBoard.getPiece(potentialMove.getEndPosition());
        chessBoard.addPiece(potentialMove.getStartPosition(), null);
        chessBoard.addPiece(potentialMove.getEndPosition(), piece);

        boolean response = (isInCheck(piece.getTeamColor())) ? true : false;
        resetMove(potentialMove, piece, takenPiece);
        return response;
    }

    public void resetMove(ChessMove move, ChessPiece movedPiece, ChessPiece takenPiece) {
        chessBoard.addPiece(move.getStartPosition(), movedPiece);
        chessBoard.addPiece(move.getEndPosition(), takenPiece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!isMovablePiece(move.getStartPosition())) {
            throw new InvalidMoveException("Error: no valid piece to move at that position");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
            chessBoard.addPiece(move.getEndPosition(), piece);
            chessBoard.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() != null) {
                promotePiece(move.getEndPosition(), move.getPromotionPiece());
            }
            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("Error: move is invalid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++ ) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> opposingMoves = piece.pieceMoves(chessBoard, position);
                    for (ChessMove move : opposingMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && noValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && noValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    private boolean noValidMoves(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece opposingPiece = chessBoard.getPiece(position);
                if (opposingPiece != null && opposingPiece.getTeamColor() == teamColor) {
                    if (!validMoves(position).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    System.out.printf("King position: %d, %d\t", i, j);
                    return new ChessPosition(i, j);
                }
            }
        }
        return null;
    }

    private void printMove(ChessMove move) {
        System.out.printf("Start position: %d, %d  End position %d, %d\n",
                move.getStartPosition().getRow() + 1,
                move.getStartPosition().getColumn() + 1,
                move.getEndPosition().getRow() + 1,
                move.getEndPosition().getColumn() + 1
        );
    }

    private boolean isMovablePiece(ChessPosition position) {
        ChessPiece piece = chessBoard.getPiece(position);
        if (piece != null && piece.getTeamColor() == teamTurn) {
            return true;
        }
        return false;
    }

    private void promotePiece(ChessPosition position, ChessPiece.PieceType promotionType) {
        ChessPiece promotionPiece = new ChessPiece(chessBoard.getPiece(position).getTeamColor(), promotionType);
        chessBoard.addPiece(position, promotionPiece);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, chessBoard);
    }
}
