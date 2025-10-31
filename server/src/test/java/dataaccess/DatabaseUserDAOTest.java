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
        try {
            databaseUserDAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            System.out.println("could not create DAO");
        }
        try {
            databaseUserDAO.clearDatabase();
        } catch (DataAccessException e) {
            System.out.println("could not clear the 'users' table");
        }
    }


    @Test
    public void setGetUserSucess() {
        UserData startUserData = new UserData("willh", "passy", "example@gmail.com");
        try {
            databaseUserDAO.addUser(startUserData);
            UserData retrivedUser = databaseUserDAO.getUser("willh");
            Assertions.assertEquals(startUserData.username(), retrivedUser.username());
            Assertions.assertEquals(startUserData.password(), retrivedUser.password());
            Assertions.assertEquals(startUserData.email(), retrivedUser.email());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            Assertions.fail("Error: threw DataAcessException");
        }
    }

    @Test
    public void addUserFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> {databaseUserDAO.addUser(new UserData(null, "passy", "example@gmail.com"));});
    }

    @Test
    public void clearUsersSucess() {
        try {
            databaseUserDAO.addUser(new UserData("willh", "passy", "example@gmail.com"));
        } catch (DataAccessException e) {
            System.out.println("Error: could not add a user");
            Assertions.fail("Error: threw DataAccessException when adding a user");
        }
        Assertions.assertDoesNotThrow(() -> {databaseUserDAO.clearDatabase();});
        UserData result = null;
        try {
            result = databaseUserDAO.getUser("willh");
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertEquals(null, result);
    }

}


