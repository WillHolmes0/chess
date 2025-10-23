package server.service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.requests.JoinGameRequest;
import model.responses.JoinGameResponse;

public class JoinGameService {
    private MemoryDatabase memoryDatabase;

    public JoinGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0 || joinGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(joinGameRequest.authorization());
        if (authData != null) {
            String username = authData.username();
            MemoryGameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
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
