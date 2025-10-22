package server.handlers;

import dataaccess.MemoryDatabase;
import server.service.LoginService;

public class LogoutHandler {
    private MemoryDatabase memoryDatabase;
    private LoginService loginService;

    public LogoutHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
//        this.logoutService = new LogoutService(memoryDatabase);
    }
}
