package model.requests;
import model.UserData;

public record RegisterRequest(String username, String password, String email) {}
