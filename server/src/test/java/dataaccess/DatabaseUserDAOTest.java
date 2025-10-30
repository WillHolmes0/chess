package dataaccess;

import com.mysql.cj.protocol.Resultset;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAOTest {
    private static DatabaseUserDAO databaseUserDAO;

    @BeforeEach
    public void clearDatabase() {
        try (Connection conn = DatabaseManager.getConnection()){
            String statement = "DROP TABLE users";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            System.out.println("Threw DataAccessException trying to clear table");
        } catch (SQLException e) {
            System.out.println("Threw SQLException trying to clear table");
        }
        try {
            databaseUserDAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            System.out.println("could not create DAO");
        }
    }

    @Test
    public void makeDatabase() {
        Assertions.assertDoesNotThrow(() -> {DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();});
    }

    @Test
    public void addUserTest() {

        UserData userData = new UserData("willh", "pass", "example@gmail.com");
        try {
            databaseUserDAO.addUser(userData);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        UserData result = new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email")
                                );
                        Assertions.assertTrue(userData.equals(result), "retrieved UserData matches expected");
                        if (rs.next()) {
                            Assertions.fail("too many entries");
                        }
                    } else {
                        Assertions.fail("Expected 1 row, got 0");
                    }
                }
            }
        } catch (SQLException e) {
            Assertions.fail("threw SQLExcetion");
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException");
        }
    }

    @Test
    public void configureTableTest() {
        Assertions.assertDoesNotThrow(() -> {DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();});
    }
    
}


