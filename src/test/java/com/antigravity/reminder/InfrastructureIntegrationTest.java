package com.antigravity.reminder;

import static org.junit.jupiter.api.Assertions.*;

import com.antigravity.reminder.dao.UserDao;
import com.antigravity.reminder.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration Test for Infrastructure Layer (UserDao). Note: Requires a running database as
 * configured in db.properties.
 */
public class InfrastructureIntegrationTest {

    static {
        System.setProperty("db.properties.file", "db-test.properties");
    }

    @org.junit.jupiter.api.BeforeAll
    static void setup() {
        org.flywaydb.core.Flyway flyway =
                org.flywaydb.core.Flyway.configure()
                        .dataSource(
                                "jdbc:h2:mem:testdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                                "sa",
                                "")
                        .load();
        flyway.migrate();
    }

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userDao.getAll();
        assertNotNull(users, "User list should not be null");
        System.out.println("Retrieved " + users.size() + " users during test.");
    }

    @Test
    void testSaveAndGetUserInfo() {
        User testUser =
                User.builder()
                        .username("test_user_" + System.currentTimeMillis())
                        .email("test@example.com")
                        .passwordHash("hash")
                        .timezone("UTC")
                        .build();

        // Save
        userDao.save(testUser);

        // Fetch back (Assume we can find by username if we added that method,
        // but here we just check if it doesn't crash)
        List<User> all = userDao.getAll();
        assertTrue(all.size() > 0);
    }
}
