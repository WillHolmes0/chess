package service;

import chess.ChessGame;
import dataaccess.*;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import service.requests.JoinGameRequest;
import service.responses.JoinGameResponse;

public class JoinGameService {
    private MemoryDatabase memoryDatabase;

    public JoinGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, UnauthorizedException, BadRequestException, AlreadyTakenException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0 || joinGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(joinGameRequest.authorization());
        if (authData != null) {
            String username = authData.username();
            ChessGame.TeamColor playerColor;
            GameData prospectiveGameData = gameDAO.getGame(joinGameRequest.gameID());
            if (prospectiveGameData == null) {
                throw new BadRequestException("Error: invalid gameID");
            }
            if (joinGameRequest.playerColor().equals("WHITE")) {
                System.out.println("white");
                playerColor = ChessGame.TeamColor.WHITE;
                if (prospectiveGameData.whiteUsername() != null) {
                    throw new AlreadyTakenException("Error: color already taken");
                }
            } else if (joinGameRequest.playerColor().equals("BLACK")) {
                System.out.println("black");
                playerColor = ChessGame.TeamColor.BLACK;
                if (prospectiveGameData.blackUsername() != null) {
                    throw new AlreadyTakenException("Error: color already taken");
                }
            } else {
                throw new BadRequestException("Error: invalid player color");
            }
            gameDAO.setPlayer(username, playerColor, joinGameRequest.gameID());
            return new JoinGameResponse();
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }

    }
}
