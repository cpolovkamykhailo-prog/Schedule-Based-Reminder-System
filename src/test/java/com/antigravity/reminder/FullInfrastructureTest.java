package com.antigravity.reminder;

import static org.junit.jupiter.api.Assertions.*;

import com.antigravity.reminder.dao.*;
import com.antigravity.reminder.model.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FullInfrastructureTest {

    static {
        System.setProperty("db.properties.file", "db-test.properties");
    }

    private static final UserDao userDao = new UserDao();
    private static final ReminderDao reminderDao = new ReminderDao();
    private static final TagDao tagDao = new TagDao();
    private static final ScheduleDao scheduleDao = new ScheduleDao();

    @BeforeAll
    static void setupDatabase() {
        // Set test properties
        System.setProperty("db.properties.file", "db-test.properties");

        // Use Flyway for robust database initialization
        org.flywaydb.core.Flyway flyway =
                org.flywaydb.core.Flyway.configure()
                        .dataSource(
                                "jdbc:h2:mem:testdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                                "sa",
                                "")
                        .load();
        flyway.migrate();
    }

    @Test
    void testUserLifecycle() {
        User user =
                User.builder()
                        .username("test_user")
                        .email("test@h2.com")
                        .passwordHash("pass")
                        .timezone("UTC")
                        .build();
        userDao.save(user);

        List<User> users = userDao.getAll();
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("test_user")));
    }

    @Test
    void testReminderAndSchedule() {
        // 1. Get existing user from sample data (ID: 1)
        Reminder reminder =
                Reminder.builder()
                        .userId(1)
                        .title("H2 Test Reminder")
                        .description("Testing integration")
                        .isEnabled(true)
                        .build();
        reminderDao.save(reminder);

        List<Reminder> all = reminderDao.getAll();
        Reminder saved =
                all.stream()
                        .filter(r -> r.getTitle().equals("H2 Test Reminder"))
                        .findFirst()
                        .orElseThrow();

        // 2. Add Schedule
        Schedule schedule =
                Schedule.builder()
                        .reminderId(saved.getId())
                        .frequency("DAILY")
                        .interval(1)
                        .startTime(LocalDateTime.now())
                        .nextRun(LocalDateTime.now().plusDays(1))
                        .build();
        scheduleDao.save(schedule);

        assertNotNull(scheduleDao.getAll());
    }

    @Test
    void testTags() {
        Tag tag = Tag.builder().name("Testing").color("#000000").build();
        tagDao.save(tag);

        List<Tag> tags = tagDao.getAll();
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("Testing")));
    }
}
