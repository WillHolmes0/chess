package server.service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.requests.JoinGameRequest;

public class JoinGameService {
    private MemoryDatabase memoryDatabase;

    public JoinGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        if (joinGameRequest.playerColor() == null || joinGameRequest.authorization() == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(joinGameRequest.authorization());
        if (authData != null) {
            String username = authData.username();
            GameDAO gameDAO = new GameDAO(memoryDatabase);
            ChessGame.TeamColor playerColor;
            if (joinGameRequest.playerColor().equals("WHITE")) {
                playerColor = ChessGame.TeamColor.WHITE;
                GameData prospectiveGameData = gameDAO.getGame(joinGameRequest.gameID());
                if (prospectiveGameData.whiteUsername() != null) {
                    throw new AlreadyTakenException("Error: already taken");
                }
            } else if (joinGameRequest.playerColor().equals("BLACK")) {
                playerColor = ChessGame.TeamColor.BLACK;
                GameData prospectiveGameData = gameDAO.getGame(joinGameRequest.gameID());
                if (prospectiveGameData.whiteUsername() != null) {
                    throw new AlreadyTakenException("Error: already taken");
                }
            } else {
                throw new BadRequestException("Error: bad request");
            }
            gameDAO.setPlayer(username, playerColor, joinGameRequest.gameID());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }

    }
}
