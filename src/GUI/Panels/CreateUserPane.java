package GUI.Panels;

import Classes.FileObj;
import Classes.PasswordGenerator;
import Classes.UserObj;
import Classes.XMLWorker;
import GUI.MainWindow;
import com.sun.tools.javac.Main;

import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CreateUserPane extends JPanel {

    CreateUserPane(){

        JPanel panel = new JPanel(new GridLayout(6,1));
        JPanel usernamePanel = new JPanel(new GridLayout(1,2));
        JPanel passwordPanel = new JPanel(new GridLayout(1,2));
        JLabel welcomeLabel = new JLabel("Заполните поля ");
        JPanel welcomePanel = new JPanel();
        JTextField userNameText = new JTextField();
        JLabel usernameLabel = new JLabel("Имя пользователя: ");
        JLabel passwordLabel = new JLabel("Пароль :");
        JTextField passwordText = new JTextField();
        JButton passwordButton = new JButton("Сгенерировать");
        JButton createButton = new JButton("Создать");
        createButton.setVisible(false);
        JLabel informLabel = new JLabel("Имя пользователя не может быть короче 4х символов!");
        JLabel informLabel1 = new JLabel("Такой пользователь уже существует!");
        JPanel informPanel = new JPanel();
        JPanel informPanel1 = new JPanel();
        informLabel1.setVisible(false);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<UserObj> usersList = XMLWorker.parseUsers();
                ArrayList<FileObj> files = XMLWorker.parseRights();
                String userName = userNameText.getText();
                String password = passwordText.getText();
                UserObj user = new UserObj(userName,password,"user");
                if ((userNameText.getText().length() > 3)){
                    if(checkExistUser(usersList, userName)){
                        usersList.add(user);
                        try {
                            XMLWorker.addUser(usersList);
                        } catch (ParserConfigurationException | TransformerException | ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            updateFilesRights(files,user);
                        } catch (FileNotFoundException | TransformerException | ParserConfigurationException e) {
                            e.printStackTrace();
                        }
                        MainWindow.delButton();
                        panel.removeAll();
                        add(new WelcomePan(1));
                        MainWindow.paintExit();
                    } else {
                        informLabel1.setVisible(true);
                        usernamePanel.setBackground(Color.pink);
                        informPanel1.setBackground(Color.pink);
                    }

                } else {
                    informLabel1.setVisible(false);
                    informLabel.setText("Имя пользователя не может быть короче 4х символов!");
                    usernamePanel.setBackground(Color.pink);
                    informPanel.setBackground(Color.pink);
                    updateUI();
                }
            }
        });

        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PasswordGenerator passwordGenerator = new PasswordGenerator(8);
                String password = passwordGenerator.getPassword().toString();
                passwordPanel.remove(passwordButton);
                passwordText.setText(password);
                passwordPanel.add(passwordText);
                createButton.setVisible(true);
                updateUI();
            }
        });

        welcomePanel.add(welcomeLabel);
        informPanel.add(informLabel);
        informPanel1.add(informLabel1);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(userNameText);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordButton);
        panel.add(welcomePanel);
        panel.add(informPanel);
        panel.add(informPanel1);
        panel.add(usernamePanel);
        panel.add(passwordPanel);
        panel.add(createButton);
        add(panel);
        updateUI();
    }

    private void updateFilesRights(ArrayList<FileObj> array, UserObj user) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ArrayList<UserObj> tmpUsers;
        for (int i = 0; i < array.size(); i++) {
            tmpUsers = array.get(i).getUsers();
            tmpUsers.add(user);
            array.get(i).setUsers(tmpUsers);
        }
        XMLWorker.applyRights(array);
    }

    private boolean checkExistUser(ArrayList<UserObj> list, String username){
        boolean result = true;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(username)){
                result = false;
            }
        }
        return result;
    }
}
