package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для создания графического интерфейса Swing и обработки событий.
 * Он предоставляет окно для конвертации сообщений, позволяя выбирать способ ввода сообщения
 * (ручной ввод или выбор из списка в базе данных) и выполнять конвертацию сообщения.
 * Также обновляет список сообщений для выбора в комбобоксе после каждой конвертации.
 */
public class SwingApp extends JFrame {


    /**
     * Конструктор окна конвертации
     * @param messages - лист с сообщениями из БД
     */
    public SwingApp(ArrayList<String> messages) {

        // Настройка окна
        super("Конвертация сообщений");
        this.setSize(800, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);


        // Объекты формы
        JRadioButton rbInput = new JRadioButton("Ручной ввод");
        JRadioButton rbDatabase = new JRadioButton("Из БД");
        JLabel lType = new JLabel("Выберите ввод:");
        JLabel lInput = new JLabel("Введите сообщение:");
        JLabel lCombo = new JLabel("Выберите сообщение:");
        JButton btnConvert = new JButton("Конвертировать");
        JTextField tfMessage = new JTextField("", 5);
        JComboBox cbMessages = new JComboBox(messages.toArray());

        // Контейнеры для объектов
        JPanel cbPanel = new JPanel();
        JPanel tfPanel = new JPanel();
        JPanel rbPanel = new JPanel();
        JPanel btnPanel = new JPanel();

        JPanel cont = new JPanel(); // контейнер для контейнеров текстового поля и комбобокса, чтобы отображалось по центру

        // Создание группы радиобаттонов
        ButtonGroup group = new ButtonGroup();
        group.add(rbInput);
        group.add(rbDatabase);

        // События радиобаттонов (выбор ручного ввода/сообщения из БД)
        rbInput.addActionListener(e -> {
            if (rbInput.isSelected()) {
                tfPanel.setVisible(true);
                cbPanel.setVisible(false);
            }
        });
        rbDatabase.addActionListener(e -> {
            if (rbDatabase.isSelected()) {
                tfPanel.setVisible(false);
                cbPanel.setVisible(true);
            }
        });

        // Добавление радиобаттонов в контейнер и добавление на форму
        cbPanel.setVisible(false);
        rbPanel.add(lType);
        rbPanel.add(rbInput);
        rbInput.setSelected(true);
        rbPanel.add(rbDatabase);
        this.getContentPane().add(BorderLayout.NORTH, rbPanel);

        // Добавление поля для ручного ввода сообщения в контейнер и на форму
        tfMessage.setColumns(50);
        tfPanel.add(lInput);
        tfPanel.add(tfMessage);
        cont.add(BorderLayout.NORTH, tfPanel);

        // Добавление комбобокса для выбора сообщений из БД в контейнер и вывод на форму
        cbPanel.add(lCombo);
        cbPanel.add(cbMessages);
        cont.add(BorderLayout.SOUTH, cbPanel);
        this.getContentPane().add(BorderLayout.CENTER, cont);

        // Событие кнопки "Конвертировать"
        btnConvert.addActionListener(e -> {
            // Определяем выбранный тип ввода
            if (rbInput.isSelected()) {
                if (!validateTtm(tfMessage.getText()) && !validateRsd(tfMessage.getText())) { // Проверка сообщения на валидность
                    JOptionPane.showMessageDialog(null, "Введённое сообщение не соответствует формату!", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                showMessage(App.convertMessage(tfMessage.getText()));
            } else {
                showMessage(App.convertMessage((String) cbMessages.getSelectedItem()));
            }
            // Проверка сообщения на уникальность
            App.checkMessageForUnique(tfMessage.getText());

            // Загрузка нового списка сообщений в комбобокс
            cbMessages.removeAllItems();
            ArrayList<String> newMessages = App.getNewMessages();
            for (String message : newMessages) {
                cbMessages.addItem(message);
            }
        });

        // Добавление кнопки в контейнер и на форму
        btnPanel.add(btnConvert);
        this.getContentPane().add(BorderLayout.SOUTH, btnPanel);
    }

    /**
     * Функция для отображения диалогового окна с конвертированным сообщением
     * @param sb - строка с сообщением для отображения
     */
    private void showMessage(StringBuilder sb) {
        JOptionPane.showMessageDialog(null, sb, "Сообщение", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Функция для проверки соответствия введённого сообщения с шаблоном RSD
     * @param message - TTM сообщение, введённое пользователем
     * @return - true, если сообщение соответствует формату RSD, false, если нет
     */
    public static boolean validateRsd(String message) {
        System.out.println("Message: " + message);
        // Очистим сообщение от пробелов и символов новой строки
        String cleanedMessage = message.replaceAll("\\s", "");
        // Создание паттерна
        String pattern = "\\$RARSD,\\d+[.]?\\d+?,\\d+[.]?\\d+?,\\d+[.]?\\d+?,\\d+[.]?\\d+?,,,,,\\d+[.]?\\d+?,\\d+[.]?\\d+?,\\d+[.]?\\d+?,[KN],[CHN],[SP]\\*\\w{1,2}";
        Pattern p = Pattern.compile(pattern);
        // Проверка на соответствие паттерну
        Matcher m = p.matcher(cleanedMessage);
        boolean result = m.find();
        System.out.println("Validation RSD result: " + result);
        return result;
    }

    /**
     * Функция для проверки соответствия введённого сообщения с шаблоном TTM
     * @param message - TTM сообщение, введённое пользователем
     * @return - true, если сообщение соответствует формату TTM, false, если нет
     */
    public static boolean validateTtm(String message) {
        System.out.println("Message: " + message);
        // Удаление пробелов
        String cleanedMessage = message.replaceAll("\\s", "");
        // Создание паттерна
        String pattern = "\\$RATTM,\\d+[.]?\\d+?,\\d+[.]?\\d+?,\\d+[.]?\\d+?,T,\\d+[.]?\\d+?,\\d+[.]?\\d+?,T,\\d+[.]?\\d+?,\\d+[.]?\\d+?,N,[bpd],[LQT],,\\d{6},A\\*\\w{1,2}";
        Pattern p = Pattern.compile(pattern);
        // Проверка на соответствие паттерну
        Matcher m = p.matcher(cleanedMessage);
        boolean result = m.find();
        System.out.println("Validation TTM result: " + result);
        return result;
    }
}
