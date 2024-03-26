package org.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {

    private final static String URL = "jdbc:mysql://localhost:3306/DBMessages";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "password";

    private Connection connection;

    public DBWorker() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("We are connected!");
            }
        } catch (SQLException e) {
            System.out.println("There is no connection... Exception! " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
