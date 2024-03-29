package org.example;

import org.database.DBWorker;
import org.example.searadar.mr231.convert.Mr231Converter;
import org.example.searadar.mr231.station.Mr231StationType;
import org.example.searadar.mr231_3.convert.Mr231_3Converter;
import org.example.searadar.mr231_3.station.Mr231_3StationType;
import ru.oogis.searadar.api.message.SearadarStationMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Практическое задание направлено на проверку умения создавать функциональные модули и работать с технической
 * документацией.
 * Задача написать парсер сообщений для протокола МР-231-3 на основе парсера МР-231.
 * Приложение к заданию файлы:
 * - Протокол МР-231.docx
 * - Протокол МР-231-3.docx
 * <p>
 * Требования:
 * 1. Перенести контрольный пример из "Протокола МР-231.docx" в метод main, по образцу в методе main;
 * 2. Проверить правильность работы конвертера путём вывода контрольного примера в консоль, по образцу в методе main;
 * 3. Создать модуль с именем mr-231-3 и добавить его в данный проект <module>../mr-231-3</module> и реализовать его
 * функционал в соответствии с "Протоколом МР-231-3.docx" на подобии модуля mr-231;
 * 4. Создать для модуля контрольный пример и проверить правильность работы конвертера путём вывода контрольного
 * примера в консоль
 *
 * <p>
 *     Примечание:
 *     1. Данные в пакете ru.oogis не изменять!!!
 *     2. Весь код должен быть покрыт JavaDoc
 */

public class App {

    private static ArrayList<String> messages = new ArrayList<>(); // Лист с сообщениями из базы
    private static Statement statement; // Переменная для совершения запросов к БД
    private static ResultSet resultSet; // Переменная для получения результата запроса БД
    private static PreparedStatement preparedStatement; // Переменная для совершения запросов к БД
    private static final String INSERT_NEW = "INSERT INTO Message (Message) VALUES(?)"; // SQL запрос к БД

    private static final Mr231_3StationType mr231_3 = new Mr231_3StationType();
    private static final Mr231_3Converter mr231_3Converter = mr231_3.createConverter();

    private static List<SearadarStationMessage> searadarMessages;

    public static void main(String[] args) {
        // Контрольный пример для МР-231
        String mr231_TTM = "$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,457362,А*42";
        String mr231_VHW = "$RAVHW,115.6,T,,,46.0,N,,*71";
        String mr231_RSD = "$RARSD,14.0,0.0,96.9,306.4,,,,,97.7,11.6,0.3,K,N,S*20"; // Ошибка в 0.3

        // Проверка работы конвертера
        Mr231StationType mr231 = new Mr231StationType();
        Mr231Converter converter = mr231.createConverter();
        searadarMessages = converter.convert(mr231_TTM);
        searadarMessages.forEach(System.out::println);
        searadarMessages = converter.convert(mr231_VHW);
        searadarMessages.forEach(System.out::println);
        searadarMessages = converter.convert(mr231_RSD);
        searadarMessages.forEach(System.out::println);


        // Контрольный пример для МР-231-3
        String mr231_3_TTM = "$RATTM,45,15.21,245.7,T,34.7,124.1,T,2.2,10.5,N,d,Q,,785146,A*42";
        String mr231_3_RSD = "$RARSD,4.5,0.0,5.1,9.5,,,,,7.5,125.1,12.0,N,H,P*20";

        // Проверка работы конвертера МР-231-3
        searadarMessages = mr231_3Converter.convert(mr231_3_TTM);
        searadarMessages.forEach(System.out::println);
        searadarMessages = mr231_3Converter.convert(mr231_3_RSD);
        searadarMessages.forEach(System.out::println);


        getMessages();

        SwingApp app = new SwingApp(messages);
        app.setVisible(true);
    }

    /**
     * Функция для получения всех сообщений из бд
     */
    private static void getMessages() {
        // Создаём объект бд
        DBWorker worker = new DBWorker();

        String query = "select Message from Message";
        //sendQuery(query);

        // выполнение запроса к бд
        try {
            if (worker.getConnection() == null)
                return;
            statement = worker.getConnection().createStatement();
            resultSet = statement.executeQuery(query);

            messages.clear();
            while (resultSet.next()) {
                messages.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("Ошибка при подключении к базе данных: " + ex.getMessage());
        } finally { // после выполнения запроса закрываем соединение с бд
            try {
                if (worker.getConnection() != null)
                    worker.getConnection().close();
            } catch(SQLException se) {
                System.err.println("Ошибка при закрытии соединения с базой данных: " + se.getMessage());
            }
            try {
                if (statement != null)
                    statement.close();
            } catch(SQLException se) {
                System.err.println("Ошибка при закрытии запроса: " + se.getMessage());
            }
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch(SQLException se) {
                System.err.println("Ошибка при закрытии результата: " + se.getMessage());
            }
        }
    }

    /**
     * Функция для проверки сообщений на существование в бд
     * если сообщение новое, то добавляем в бд
     * @param message - сообщение станции
     */
    public static void checkMessageForUnique(String message) {
        // Если сообщение уже было, то пропускаем
        if (messages.contains(message))
            return;

        //messages.add(message);

        // Создаём объект бд
        DBWorker worker = new DBWorker();

        // выполнение запроса к бд
        try {
            preparedStatement = worker.getConnection().prepareStatement(INSERT_NEW);
            preparedStatement.setString(1, message);

            preparedStatement.execute();
        } catch (SQLException ex) {
            System.err.println("Ошибка при подключении к базе данных: " + ex.getMessage());
        } finally { // после выполнения запроса закрываем соединение с бд
            try {
                if (worker.getConnection() != null)
                    worker.getConnection().close();
            } catch(SQLException se) {
                System.err.println("Ошибка при закрытии соединения с базой данных: " + se.getMessage());
            }
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch(SQLException se) {
                System.err.println("Ошибка при закрытии запроса: " + se.getMessage());
            }
        }
    }

    /**
     * Функция для запроса всех сообщений из бд
     * (если при конвертации сообщение было новым и только добавлено в бд,
     * то запрашиваем вновь все сообщения для отображения в ComboBox)
     * @return - ранее конвертированные сообщения из бд
     */
    public static ArrayList<String> getNewMessages() {
        getMessages();
        return messages;
    }

    /**
     * Функция конвертации сообщений. Вызывается из графического интерфейса
     * @param message - сообщение от станции
     * @return - конвертированное сообщение
     */
    public static StringBuilder convertMessage(String message) {

        searadarMessages = mr231_3Converter.convert(message);

        StringBuilder sb = new StringBuilder();
        for (SearadarStationMessage msg : searadarMessages) {
            sb.append(msg.toString()).append("\n");
        }
        return sb;
    }


    //    private static void sendQuery(String query) {
//        DBWorker worker = new DBWorker();
//        try {
//            statement = worker.getConnection().createStatement();
//            resultSet = statement.executeQuery(query);
//
//            messages.clear();
//            while (resultSet.next()) {
//                messages.add(resultSet.getString(2));
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            try { worker.getConnection().close(); } catch(SQLException se) { /*can't do anything */ }
//            try { statement.close(); } catch(SQLException se) { /*can't do anything */ }
//            try { resultSet.close(); } catch(SQLException se) { /*can't do anything */ }
//        }
//    }
}
