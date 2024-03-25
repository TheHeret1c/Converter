package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    private JButton btn = new JButton("Press");
    private JTextField input = new JTextField("", 5);
    private JLabel label = new JLabel("Input:");
    private JRadioButton rb1 = new JRadioButton("Male");
    private JRadioButton rb2 = new JRadioButton("Female");
    private JCheckBox chkbox = new JCheckBox("Check", false);

    public SwingApp() {
        super("Simple Example");
        this.setBounds(100, 100, 250, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));
        container.add(label);
        container.add(input);

        ButtonGroup group = new ButtonGroup();
        group.add(rb1);
        group.add(rb2);
        container.add(rb1);
        rb1.setSelected(true);
        container.add(rb2);

        container.add(chkbox);

        btn.addActionListener(new ButtonEventListener());
        container.add(btn);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String message = "";
            message += "Button was pressed\n";
            message += "Text is " + input.getText() + "\n";
            message += (rb1.isSelected() ? "Radio 1" : "Radio 2") + " is selected!\n";
            message += "CheckBox is " + ((chkbox.isSelected()) ? "checked" : "unchecked");
            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
