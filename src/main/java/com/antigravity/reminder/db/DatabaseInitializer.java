package com.antigravity.reminder.db;

import java.io.InputStream;
import java.util.Properties;
import org.flywaydb.core.Flyway;

public class DatabaseInitializer {
    public static void runMigrations() {
        Properties props = new Properties();
        try (InputStream input =
                DatabaseInitializer.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                Flyway flyway = Flyway.configure().dataSource(url, user, pass).load();
                flyway.migrate();
                System.out.println("Database migrations applied successfully.");
            }
        } catch (Exception e) {
            System.err.println("Failed to run migrations: " + e.getMessage());
        }
    }
}
