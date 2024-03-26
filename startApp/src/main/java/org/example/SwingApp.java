package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

//        cbMessages = new JComboBox<>(messages.toArray());
//
//        Container container = this.getContentPane();
//        container.setLayout(new GridLayout(4, 2, 2, 2));
//        container.add(label);
//        container.add(input);
//
//        ButtonGroup group = new ButtonGroup();
//        group.add(rb1);
//        group.add(rb2);
//        container.add(rb1);
//        rb1.setSelected(true);
//        container.add(rb2);
//
//        container.add(chkbox);
//
//        btn.addActionListener(e -> {
//            String message = "";
//            message += "Button was pressed\n";
//            message += "Text is " + input.getText() + "\n";
//            message += (rb1.isSelected() ? "Radio 1" : "Radio 2") + " is selected!\n";
//            message += "CheckBox is " + ((chkbox.isSelected()) ? "checked" : "unchecked");
//            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
//        });
//        container.add(btn);
//        container.add(cbMessages);
    }

//    class ButtonEventListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            String message = "";
//            message += "Button was pressed\n";
//            message += "Text is " + input.getText() + "\n";
//            message += (rb1.isSelected() ? "Radio 1" : "Radio 2") + " is selected!\n";
//            message += "CheckBox is " + ((chkbox.isSelected()) ? "checked" : "unchecked");
//            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
//        }
//    }

    /**
     * Функция для отображения диалогового окна с конвертированным сообщением
     * @param sb - строка с сообщением для отображения
     */
    private void showMessage(StringBuilder sb) {
        JOptionPane.showMessageDialog(null, sb, "Сообщение", JOptionPane.PLAIN_MESSAGE);
    }
}
