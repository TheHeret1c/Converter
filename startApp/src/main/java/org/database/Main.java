package org.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    private final static String URL = "jdbc:mysql://localhost:3306/DBMessages??user=root&password=password&serverTimezone=UTC";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "password";

    public static void main(String[] args) {
        Connection connection;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("We are connected!");
            }
        } catch (SQLException e) {
            System.out.println("There is no connection... Exception! " + e.getMessage());
        }
    }
}
