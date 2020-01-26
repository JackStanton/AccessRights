package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UncPass extends JFrame {
    private static final int WINDOW_HEIGHT = 130;
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_POSITION_X = 620;
    private static final int WINDOW_POSITION_Y = 350;

    public UncPass(){
        setLocation(WINDOW_POSITION_X,WINDOW_POSITION_Y);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle("Информация");
        JLabel label = new JLabel("Неправильно введен пароль!");
        JButton button = new JButton("ОК");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        });
        JPanel panel = new JPanel(new GridLayout(2,1));
        panel.add(label, BorderLayout.CENTER);
        panel.add(button);
        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }
}
