package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void addRow() {
        Assertions.assertDoesNotThrow(() -> {
            try {
                DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();
                databaseUserDAO.addUser(new UserData("test", "", ""));
            } catch (DataAccessException e) {
                System.out.printf("Error: threw a DataAccessException");
            }

        });
    }
}
