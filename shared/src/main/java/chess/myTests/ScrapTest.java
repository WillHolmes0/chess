package chess.myTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class ScrapTest {

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ChessBoard newBoard = game.getBoard();
        newBoard.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        newBoard.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        game.setBoard(newBoard);
        Collection<ChessMove> validMoves = game.validMoves(new ChessPosition(8, 8));
    }

}


