package com.antigravity.reminder.db;

import java.io.InputStream;
import java.util.Properties;
import org.flywaydb.core.Flyway;

public class DatabaseInitializer {
    public static void runMigrations() {
        String propFile = System.getProperty("db.properties.file", "db.properties");
        Properties props = new Properties();
        try (InputStream input =
                DatabaseInitializer.class.getClassLoader().getResourceAsStream(propFile)) {
            if (input != null) {
                props.load(input);
                String url = DbConfig.resolveUrl(props.getProperty("db.url"));
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                Flyway flyway = Flyway.configure().dataSource(url, user, pass).load();
                flyway.migrate();
                System.out.println("Database migrations applied successfully.");
            } else {
                System.err.println("Unable to find " + propFile);
            }
        } catch (Exception e) {
            System.err.println("Failed to run migrations: " + e.getMessage());
        }
    }
}
