package GUI.Panels;

import Classes.DBWorker;
import Classes.UserObj;
import Classes.VBSUtility;
import GUI.MainWindow;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;

public class WelcomePan extends JPanel {
    int fontSize = 40;
    JLabel welcomeLabel = new JLabel();
    public static String[] users;
    JButton enterButton;
    JComboBox<String> comboBox;
    JPasswordField passText;
    public static ArrayList<UserObj> usersList;
    private String autUser;
    public static String autUser1;
    public static DocumentsPane documentsPane;

    public WelcomePan(int init) throws SQLException, ClassNotFoundException {
        if (checkProp()){
            usersList = DBWorker.readUsers();
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

                    autUser = usersList.get(userName).getName();
                    autUser1 = autUser;
                    if (usersList.get(userName).getPass().equals(password)){
                        try {
                            documentsPane = new DocumentsPane(autUser);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        removeAll();
                        updateUI();
                        add(documentsPane);
                        MainWindow.delButton();
                        MainWindow.paintBack(0);

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
        }else {
            JLabel alertlabel = new JLabel("Данную программу нельзя использовать на этом ПК");
            alertlabel.setFont(new Font(alertlabel.getFont().getName(), Font.PLAIN, fontSize));
            add(alertlabel);
            setBackground(Color.pink);
        }

    }

    private void getUsers(){
        users = new String[usersList.size()];
        for (int i = 0; i < usersList.size(); i++) {
            users[i] = usersList.get(i).getName();
        }
    }

    private boolean checkProp() throws SQLException {
        String[] propNames = new String[] { "Name","MaxClockSpeed", "ProcessorId"};
        Map<String, String> systemMap = VBSUtility.printComputerSystemProductInfo(propNames);
        Map<String, String> autMap = DBWorker.readAut();
        String systemUserName = System.getProperty("user.name");
        String systemCpuId = systemMap.get("ProcessorId");
        String systemAutUserName = autMap.get("username");
        String systemAutCpuId = autMap.get("cpuID");
        return (systemUserName.equals(systemAutUserName)&&systemCpuId.equals(systemAutCpuId));
    }

}
