package org.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс DBWorker представляет собой рабочего для взаимодействия с базой данных.
 * Он предоставляет методы для установки соединения с базой данных и получения объекта соединения.
 */
public class DBWorker {

    //private final static String URL = "jdbc:mysql://localhost:3306/DBMessages";
    // private final static String URL = "jdbc:mysql://host.docker.internal:3306/DBMessages"; // Windows docker
    //private final static String URL = "jdbc:mysql://" + System.getenv("HOST_IP") + ":3306/DBMessages"; // Linux docker

    /**
     * URL для подключения к базе данных.
     * Для использования в Docker установлено значение "jdbc:mysql://db:3306/DBMessages", где "db" - имя сервиса базы данных в Docker Compose.
     */
    private final static String URL = "jdbc:mysql://db:3306/DBMessages";
    /** Имя пользователя для доступа к базе данных. */
    private final static String USERNAME = "root";
    /** Пароль для доступа к базе данных. */
    private final static String PASSWORD = "password";

    /** Объект соединения для взаимодействия с базой данных. */
    private Connection connection;

    /**
     * Конструктор объекта DBWorker, который устанавливает соединение с базой данных.
     * Выводит сообщение, если соединение установлено успешно.
     */
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

    /**
     * Возвращает объект соединения для взаимодействия с базой данных.
     * @return Объект соединения.
     */
    public Connection getConnection() {
        return connection;
    }
}
