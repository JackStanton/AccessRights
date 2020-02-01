package GUI.Panels;

import Classes.DBWorker;
import Classes.FileObj;
import Classes.UserObj;
import GUI.MainWindow;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransferPan extends JPanel {
    String[] users = WelcomePan.users;
    String[] usersTrim = new String[users.length-1];
    String authUser = "";
    public static ArrayList<FileObj> files = DocumentsPane.files;
    int selectedUser = -1;
    int indexAutUser = -1;
    String read = DocumentsPane.read;
    String write = DocumentsPane.write;
    String transfer = DocumentsPane.transfer;
    String full = DocumentsPane.full;
    String filename = DocumentsPane.filename;
    TransferPan(String authUser){
        this.authUser = authUser;
        removeAll();
        updateUI();
        delAutUser();
        search(users,authUser);
        JLabel titleLabel = new JLabel("Выберите пользователя : ");
        JPanel panel = new JPanel(new GridLayout(5,1));
        JPanel panel1 = new JPanel(new GridLayout(1,2));
        JComboBox<String> comboBox = new JComboBox<>(usersTrim);
        JButton okButton = new JButton("Применить");
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RightsPane rightsPane = new RightsPane(authUser,read,write,transfer,full,filename);
                removeAll();
                updateUI();
                add(rightsPane);
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedUser = comboBox.getSelectedIndex();
                int selectedId = 0;
                try {
                    selectedId = searchSelected(selectedUser);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    DBWorker.applyRights(authUser,selectedId,filename);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                DocumentsPane.panel.removeAll();
                MainWindow.paintBack(0);
                removeAll();
                updateUI();
                DocumentsPane docPan = null;
                try {
                    docPan = new DocumentsPane(authUser);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                add(docPan);
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(1,2));
        btnPanel.add(okButton);
        btnPanel.add(backButton);
        panel1.add(titleLabel);
        panel1.add(comboBox);
        panel.add(panel1);
        panel.add(btnPanel);
        add(panel);
        updateUI();
    }

    private void delAutUser(){
        int j = 0;
        for (int i = 0; i < users.length; i++) {
            if (!users[i].equals(authUser)){
                 usersTrim[j] = users[i];
                j++;
            }
        }
    }


    private void search(String[] array, String authUser){
        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(authUser)){
                indexAutUser = i;
            }
        }
    }

    private int searchSelected(int select) throws SQLException, ClassNotFoundException {

        String name = usersTrim[select];
        ArrayList<UserObj> userList = DBWorker.readUsers();
        int index = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(name)){
                index = userList.get(i).getId();
            }
        }
        return index;
    }
}
