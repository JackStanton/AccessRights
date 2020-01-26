package GUI.Panels;

import Classes.UserObj;
import Classes.XMLWorker;
import GUI.MainWindow;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WelcomePan extends JPanel {
    JLabel welcomeLabel = new JLabel();
    public static String[] users;
    JButton enterButton;
    JComboBox<String> comboBox;
    JPasswordField passText;
    public static ArrayList<UserObj> usersList;
    public static String autUser;
    public static DocumentsPane documentsPane;

    public WelcomePan(int init){
        usersList = XMLWorker.parseUsers();
        removeAll();
        updateUI();
        getUsers();
        welcomeLabel.setText("Добро пожаловать!");
        JPanel welcomePanel = new JPanel();
        JLabel comboBoxLabel = new JLabel("Выберите пользователя: ");
        JLabel passLabel = new JLabel("Введите пароль: ");
        JPanel panel = new JPanel(new GridLayout(6,1));
        JPanel panelCombo = new JPanel(new GridLayout(1,2));
        JPanel panelPass = new JPanel(new GridLayout(1,2));
        comboBox = new JComboBox<>(users);
        passText = new JPasswordField();
        enterButton = new JButton("Вход");
        JButton createUButton = new JButton("Создать пользователя");
        JLabel informLabel = new JLabel();
        JPanel informPanel = new JPanel();
        informPanel.setVisible(false);
        if (init  == 1){
            informLabel = new JLabel("Пользоваетль успешно создан");
            informPanel.setBackground(Color.green);
            informPanel.setVisible(true);
        }

        createUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CreateUserPane createUserPane = new CreateUserPane();
                removeAll();
                updateUI();
                add(createUserPane);
                MainWindow.delButton();
                MainWindow.paintBack(1);
            }
        });

        JLabel finalInformLabel = informLabel;
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String password = String.copyValueOf(passText.getPassword());
                int userName = comboBox.getSelectedIndex();

                if (usersList.get(userName).getPass().equals(password)){
                    documentsPane = new DocumentsPane();
                    removeAll();
                    updateUI();
                    add(documentsPane);
                    MainWindow.delButton();
                    MainWindow.paintBack(0);
                    autUser = usersList.get(userName).getName();
                    MainWindow.printReset();
                } else {
                    finalInformLabel.setText("Неправильно введен пароль!");
                    informPanel.setVisible(true);
                    informPanel.setBackground(Color.pink);
                    panelPass.setBackground(Color.pink);
                }
            }
        });
        welcomePanel.add(welcomeLabel);
        informPanel.add(informLabel);
        panelCombo.add(comboBoxLabel);
        panelCombo.add(comboBox);
        panelPass.add(passLabel);
        panelPass.add(passText);
        panel.add(welcomePanel);
        panel.add(informPanel);
        panel.add(panelCombo);
        panel.add(panelPass);
        panel.add(enterButton);
        panel.add(createUButton);
        add(panel);
        updateUI();
    }

    private void getUsers(){
        users = new String[usersList.size()];
        for (int i = 0; i < usersList.size(); i++) {
            users[i] = usersList.get(i).getName();
        }
    }
}
