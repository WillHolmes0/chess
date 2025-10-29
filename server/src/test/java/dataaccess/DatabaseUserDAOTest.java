package dataaccess;

import org.junit.jupiter.api.Test;

public class DatabaseUserDAOTest {

    @Test
    public void makeDatabase() {
        try {
            DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            System.out.println("failed to create database");
        }
    }
}
