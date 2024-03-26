package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.example.App;

public class SwingApp extends JFrame {

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("My Swing App");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            JButton button = new JButton("Click Me!");
//            button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Button Clicked!"));
//
//            frame.getContentPane().add(button);
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }

    //ToDo...

    private JButton btn = new JButton("Press");
    private JTextField input = new JTextField("", 50);
    private JLabel label = new JLabel("Input:");
    private JRadioButton rbInput = new JRadioButton("Ручной ввод");
    private JRadioButton rbDatabase = new JRadioButton("Из БД");
    private JCheckBox chkbox = new JCheckBox("Check", false);


    private JLabel lType = new JLabel("Выберите ввод:");
    private JLabel lInput = new JLabel("Введите сообщение:");
    private JLabel lCombo = new JLabel("Выберите сообщение:");
    private JButton btnConvert = new JButton("Конвертировать");
    private JTextField tfMessage = new JTextField("", 5);
    private JLabel lMessage = new JLabel("Сообщение:");
    private JComboBox cbMessages;
    private JPanel cbPanel = new JPanel();
    private JPanel tfPanel = new JPanel();
    private JPanel rbPanel = new JPanel();
    private JPanel btnPanel = new JPanel();

    private JPanel cont = new JPanel();

    public SwingApp(ArrayList<String> messages) {

        //ToDo...

        super("Simple Example");
        this.setSize(800, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        ButtonGroup group = new ButtonGroup();
        group.add(rbInput);
        group.add(rbDatabase);

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

        cbPanel.setVisible(false);
        rbPanel.add(lType);
        rbPanel.add(rbInput);
        rbInput.setSelected(true);
        rbPanel.add(rbDatabase);
        this.getContentPane().add(BorderLayout.NORTH, rbPanel);

        tfMessage.setColumns(50);
        tfPanel.add(lInput);
        tfPanel.add(tfMessage);
        //this.getContentPane().add(BorderLayout.BEFORE_FIRST_LINE, tfPanel);
        cont.add(BorderLayout.NORTH, tfPanel);

        cbMessages = new JComboBox(messages.toArray());
        cbPanel.add(lCombo);
        cbPanel.add(cbMessages);
        //this.getContentPane().add(BorderLayout.CENTER, cbPanel);
        cont.add(BorderLayout.SOUTH, cbPanel);
        this.getContentPane().add(BorderLayout.CENTER, cont);

        btnConvert.addActionListener(e -> {

        });
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
}
