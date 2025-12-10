import chess.*;
import websocket.NoMatchException;

import java.util.ArrayList;
import java.util.Collection;

public class UiBase {

    protected String drawGameBoard(ChessGame chessGame, String perspective) {
        return drawGameBoard(chessGame, perspective, new ArrayList<>(), null);
    }

    protected String drawGameBoard(ChessGame chessGame, String perspective, Collection<ChessPosition> highlighedSquares, ChessPosition highlightedPieceSquare) {
        ChessBoard chessBoard = chessGame.getBoard();
        String chessBoardString = "";
        int square = 0;
        if (perspective.equals("white") || perspective.equals("observer")) {
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
                        String backgroundColor;
                        if (square % 2 == 1) {
                            backgroundColor = (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            backgroundColor = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        chessBoardString += highlightIfValidMove(new ChessPosition(i, j), backgroundColor, highlighedSquares, highlightedPieceSquare);
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
                        String backgroundColor;
                        if (square % 2 == 1) {
                            backgroundColor = (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            backgroundColor = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        chessBoardString += highlightIfValidMove(new ChessPosition(i, j), backgroundColor, highlighedSquares, highlightedPieceSquare);
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

    private String highlightIfValidMove(ChessPosition chessPosition, String positionColor, Collection<ChessPosition> highlightedPositions, ChessPosition startPosition) {
        if (highlightedPositions.contains(chessPosition)) {
            if (positionColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
                return EscapeSequences.SET_BG_COLOR_BRIGHT_BLUE;
            }
            return EscapeSequences.SET_BG_COLOR_LIGHT_BLUE;
        } else if (chessPosition.equals(startPosition)) {
            return EscapeSequences.SET_BG_COLOR_RED;
        }
        return positionColor;
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

    public String teamColorToString(ChessGame.TeamColor teamColor) {
        return (teamColor == ChessGame.TeamColor.WHITE) ? "white" : "black";
    }

    public ChessGame.TeamColor stringToTeamColor(String color) throws NoMatchException {
        if (color.equals("white")) {
            return ChessGame.TeamColor.WHITE;
        } else if (color.equals("black")) {
            return ChessGame.TeamColor.BLACK;
        }
        throw new NoMatchException("Error: the color given was not valid");
    }

    public ChessPiece.PieceType stringToChessPiece(String piece) throws NoMatchException {
        return switch (piece) {
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "king" -> ChessPiece.PieceType.KING;
            case "pawn" -> ChessPiece.PieceType.PAWN;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new NoMatchException("Error: The entered chess piece type is invalid");
        };
    }

}
