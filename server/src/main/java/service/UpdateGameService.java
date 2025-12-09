package service;

import chess.*;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.UpdateGameRequest;
import responses.CreateGameResponse;
import responses.UpdateGameResponse;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class UpdateGameService {
    MemoryDatabase memoryDatabase;

    public UpdateGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public UpdateGameResponse updateGame(UpdateGameRequest updateGameRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (updateGameRequest.chessMove() == null || updateGameRequest.gameID() == 0 || updateGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(updateGameRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(updateGameRequest.gameID());
        try {
//            gameData.game().validMoves(updateGameRequest.chessMove().getStartPosition());
            validateTurn(authData.username(), gameData);
            gameData.game().makeMove(updateGameRequest.chessMove());
            gameDAO.updateGame(gameData);
            return new UpdateGameResponse(
                    authData.username(),
                    chessPieceToString(gameData.game().getBoard().getPiece(updateGameRequest.chessMove().getEndPosition())),
                    convertCord(updateGameRequest.chessMove().getStartPosition()),
                    convertCord(updateGameRequest.chessMove().getEndPosition()),
                    gameData);
        } catch (InvalidMoveException e) {
            throw new BadRequestException("Error: invalid move supplied");
        }

    }

    private ChessGame.TeamColor getPlayerColor(String username, GameData gameData) {
        if (username.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

    private void validateTurn(String username, GameData gameData) throws UnauthorizedException {
        ChessGame.TeamColor turn = gameData.game().getTeamTurn();
        ChessGame.TeamColor playerColor = getPlayerColor(username, gameData);
        if (playerColor == null) {
            throw new UnauthorizedException("Error: you cannot make a move as an observer");
        } else if (playerColor == turn) {
            return;
        } else {
            throw new UnauthorizedException("Error: it is not your turn");
        }
    }

    public String chessPieceToString(ChessPiece chessPiece) {
        return switch (chessPiece.getPieceType()) {
            case ChessPiece.PieceType.PAWN -> "pawn";
            case ChessPiece.PieceType.BISHOP -> "bishop";
            case ChessPiece.PieceType.KING -> "king";
            case ChessPiece.PieceType.KNIGHT -> "knight";
            case ChessPiece.PieceType.QUEEN -> "Queen";
            default -> "rook";
        };
    }

    private String numberToLetter(int number) {
        return switch (number) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            default -> "h";
        };
    }



    private int increaseCord(int number) {
        return number + 1;
    }

    private String convertCord(ChessPosition chessPosition) {
        String x = numberToLetter(increaseCord(chessPosition.getColumn()));
        String y = String.valueOf(increaseCord(chessPosition.getRow()));
        return x + y;
    }


}
