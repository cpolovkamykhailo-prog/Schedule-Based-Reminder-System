package com.antigravity.reminder.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** Custom implementation of a Database Connection Pool. */
public class CustomConnectionPool {
    private static CustomConnectionPool instance;
    private final BlockingQueue<Connection> pool;
    private final int poolSize;

    private CustomConnectionPool() {
        String propFile = System.getProperty("db.properties.file", "db.properties");
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propFile)) {
            if (input == null) {
                throw new RuntimeException("Unable to find db.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading db.properties", e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        String driver = props.getProperty("db.driver", "org.postgresql.Driver");
        this.poolSize = Integer.parseInt(props.getProperty("db.pool.size", "10"));
        this.pool = new LinkedBlockingQueue<>(poolSize);

        try {
            Class.forName(driver);
            for (int i = 0; i < poolSize; i++) {
                pool.add(DriverManager.getConnection(url, user, pass));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing connection pool", e);
        }
    }

    public static synchronized CustomConnectionPool getInstance() {
        if (instance == null) {
            instance = new CustomConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            pool.offer(connection);
        }
    }

    public void shutdown() throws SQLException {
        for (Connection conn : pool) {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
    }
}
