import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class UiBase {

    protected String drawGameBoard(ChessGame chessGame, ChessGame.TeamColor perspective) {
        ChessBoard chessBoard = chessGame.getBoard();
        String chessBoardString = "";
        int square = 0;
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int i = 9; i >= 0; i--) {
                for (int j = 0; j <= 9; j++) {
                    if (j == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "   ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (j == 9) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "  ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (i == 9 || i == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        int asciiCode = 0x40 + j;
                        chessBoardString += String.format(" %c  ", asciiCode);
                    } else {
                        if (square % 2 == 1) {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                        chessBoardString += getPieceString(chessPiece);
                    }
                    square++;
                }
                square++;
                chessBoardString += (EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 9; j >= 0; j--) {
                    if (j == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "  ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (j == 9) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "   ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (i == 9 || i == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        int asciiCode = 0x40 + j;
                        chessBoardString += String.format(" %c  ", asciiCode);
                    } else {
                        if (square % 2 == 1) {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                        chessBoardString += getPieceString(chessPiece);
                    }
                    square++;
                }
                square++;
                chessBoardString += (EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return chessBoardString;
    }


    private String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        String chessPieceString = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            chessPieceString += EscapeSequences.SET_TEXT_COLOR_WHITE;
        } else {
            chessPieceString += EscapeSequences.SET_TEXT_COLOR_BLACK;
        }
        chessPieceString += switch (piece.getPieceType()) {
            case ChessPiece.PieceType.ROOK -> EscapeSequences.BLACK_ROOK;
            case ChessPiece.PieceType.KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case ChessPiece.PieceType.BISHOP -> EscapeSequences.BLACK_BISHOP;
            case ChessPiece.PieceType.KING -> EscapeSequences.BLACK_KING;
            case ChessPiece.PieceType.PAWN -> EscapeSequences.BLACK_PAWN;
            case ChessPiece.PieceType.QUEEN -> EscapeSequences.BLACK_QUEEN;
        };
        return chessPieceString;
    }
}
