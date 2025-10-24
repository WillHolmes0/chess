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
    public Collection<ChessMove> validMoves(ChessPosition startPosition)  {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : allMoves) {
            if (!putsInCheck(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    public boolean movesIntoCheck(ChessPosition potentialPosition, TeamColor teamColor) {

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition opposingPosition = new ChessPosition(i, j);
                ChessPiece opposingPiece = chessBoard.getPiece(opposingPosition);
                if (opposingPiece.getTeamColor() != teamColor) {
                   Collection<ChessMove> opposingMoves = opposingPiece.pieceMoves(chessBoard, opposingPosition);
                   for (ChessMove opposingMove : opposingMoves) {
                       if (opposingMove.getEndPosition() == potentialPosition) {
                           return true;
                       }
                   }
                }
            }
        }
        return false;
    }

    public boolean putsInCheck(ChessMove potentialMove) {
        ChessPiece piece = chessBoard.getPiece(potentialMove.getStartPosition());
        ChessPiece takenPiece = chessBoard.getPiece(potentialMove.getEndPosition());
        chessBoard.addPiece(potentialMove.getStartPosition(), null);
        chessBoard.addPiece(potentialMove.getEndPosition(), piece);
        boolean response = (isInCheck(teamTurn)) ? true : false;
        resetMove(potentialMove, piece, takenPiece);
        return response;
    }

    public void resetMove(ChessMove move, ChessPiece movedPiece, ChessPiece takenPiece) {
        chessBoard.addPiece(move.getStartPosition(), movedPiece);
        chessBoard.addPiece(move.getEndPosition(), takenPiece);
    }

    public boolean opensToCheck(ChessPosition currentPosition, TeamColor teamColor) {
        ChessPiece piece = chessBoard.getPiece(currentPosition);
        chessBoard.addPiece(currentPosition, null);
        if (isInCheck(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
            chessBoard.addPiece(move.getEndPosition(), piece);
            chessBoard.addPiece(move.getStartPosition(), null);
        }
        throw new InvalidMoveException("Error: move is invalid");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <=8; j++ ) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(position);
                if (piece.getTeamColor() != teamColor) {
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
        if (isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if (!(validMoves(position).isEmpty()) && chessBoard.getPiece(position).getTeamColor() != teamColor) {
                        return false;
                    }
                }
            }
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
        if (!isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if (!(validMoves(position).isEmpty()) && chessBoard.getPiece(position).getTeamColor() != teamColor) {
                        return false;
                    }
                }
            }
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

    private ChessPosition findKing() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; i++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                return new ChessPosition(i, j);
            }
        }
        return null;
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
